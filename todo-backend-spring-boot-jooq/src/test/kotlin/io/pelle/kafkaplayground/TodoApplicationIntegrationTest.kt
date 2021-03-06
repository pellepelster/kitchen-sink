package io.pelle.kafkaplayground

import com.jayway.jsonpath.JsonPath
import io.pelle.todo.TodoApplication
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TodoApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoApplicationIntegrationTest {

    @Autowired
    private lateinit var mvc: MockMvc

    init {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true")
    }

    @Test
    public fun testRegistration() {

        val user = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()

        mvc.perform(
            post("/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$password" }""")
        )
            .andExpect(status().`is`(204))

        mvc.perform(
            post("/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$password" }""")
        )
            .andExpect(status().is2xxSuccessful)
    }

    @Test
    public fun testInvalidLogin() {

        mvc.perform(
            post("/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "YYY", "password": "XXX" }""")
        )
            .andExpect(status().`is`(401))
    }

    @Test
    public fun testListWorkflow() {
        val user1Auth: String = createUserAndLogin()
        val user2Auth: String = createUserAndLogin()

        // initially there should no lists at all
        mvc.perform(
            get("/lists")
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))

        // lets create a list
        val list1Name = UUID.randomUUID().toString()
        val list1Result = mvc.perform(
            post("/lists")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "$list1Name" }""")
        )
            .andExpect(status().`is`(201))
            .andExpect(jsonPath("$.name", `is`(list1Name))).andReturn().response
        val list1Id: String = JsonPath.read(list1Result.contentAsString, "$.id")

        // now we expect exactly one list
        mvc.perform(get("/lists").header("Authorization", "Bearer: $user1Auth"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(1)))
            .andExpect(jsonPath("$[0].name", `is`(list1Name)))

        // user 2 should not see anything
        mvc.perform(get("/lists").header("Authorization", "Bearer: $user2Auth"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))

        // user 2 should not be able to delete it
        mvc.perform(delete("/lists/$list1Id").header("Authorization", "Bearer: $user2Auth"))
            .andExpect(status().isForbidden)

        // try to delete non existing list
        mvc.perform(delete("/lists/79543e6d-e074-40af-a491-10dd6b0d9b08").header("Authorization", "Bearer: $user1Auth"))
            .andExpect(status().isNotFound)

        // and delete it immediately
        mvc.perform(delete("/lists/$list1Id").header("Authorization", "Bearer: $user1Auth"))
            .andExpect(status().is2xxSuccessful)

        // now it should be gone
        mvc.perform(get("/lists").header("Authorization", "Bearer: $user1Auth"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))

        // create a new one
        val list2Name = UUID.randomUUID().toString()
        val response = mvc.perform(
            post("/lists")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "$list2Name" }""")
        )
            .andExpect(status().`is`(201)).andReturn().response
        val list2Id: String = JsonPath.read(response.contentAsString, "$.id")

        // user 2 should not be able to add an item
        mvc.perform(
            post("/lists/$list2Id")
                .header("Authorization", "Bearer: $user2Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "${UUID.randomUUID()}" }""")
        )
            .andExpect(status().`is`(403))

        // add to invalid list should fail
        mvc.perform(
            post("/lists/bd02ef7b-9e13-4ccc-bc7c-b53eff1f4907")
                .header("Authorization", "Bearer: $user2Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "${UUID.randomUUID()}" }""")
        )
            .andExpect(status().`is`(404))

        // and add an item
        val item1Name = "AAA"
        val item1AddResult = mvc.perform(
            post("/lists/$list2Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "$item1Name" }""")
        )
            .andExpect(status().`is`(201))
            .andExpect(jsonPath("$.items.length()", `is`(1)))
            .andExpect(jsonPath("$.items[0].name", `is`(item1Name)))
            .andExpect(jsonPath("$.items[0].done", `is`(false)))
            .andReturn().response
        val item1Id: String = JsonPath.read(item1AddResult.contentAsString, "$.items[0].id")

        // get the list
        mvc.perform(
            get("/lists/$list2Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(200))
            .andExpect(jsonPath("$.items.length()", `is`(1)))
            .andExpect(jsonPath("$.items[0].name", `is`(item1Name)))
            .andExpect(jsonPath("$.items[0].done", `is`(false)))
            .andReturn().response

        // user 2 should not be able to update item1
        mvc.perform(
            patch("/lists/$list2Id/items/$item1Id")
                .header("Authorization", "Bearer: $user2Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "newnameforbidden" }""")
        )
            .andExpect(status().`is`(403))

        // update name of item1
        val item1NewName = "BBB"
        mvc.perform(
            patch("/lists/$list2Id/items/$item1Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "$item1NewName" }""")
        )
            .andExpect(status().`is`(200))
            .andExpect(jsonPath("$.items.length()", `is`(1)))
            .andExpect(jsonPath("$.items[0].name", `is`(item1NewName)))
            .andExpect(jsonPath("$.items[0].done", `is`(false)))

        // update status if item1
        mvc.perform(
            patch("/lists/$list2Id/items/$item1Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "done": "true" }""")
        )
            .andExpect(status().`is`(200))
            .andExpect(jsonPath("$.items.length()", `is`(1)))
            .andExpect(jsonPath("$.items[0].name", `is`(item1NewName)))
            .andExpect(jsonPath("$.items[0].done", `is`(true)))

        // and a second item
        val item2Name = "AAA"
        mvc.perform(
            post("/lists/$list2Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "$item2Name" }""")
        )
            .andExpect(status().`is`(201))
            .andExpect(jsonPath("$.items.length()", `is`(2)))
            .andExpect(jsonPath("$.items[0].name", `is`(item2Name)))
            .andExpect(jsonPath("$.items[1].name", `is`(item1NewName)))

        // lets delete item1
        mvc.perform(
            delete("/lists/$list2Id/items/$item1Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(200))
            .andExpect(jsonPath("$.items.length()", `is`(1)))
            .andExpect(jsonPath("$.items[0].name", `is`("AAA")))

        // delete list 2
        mvc.perform(delete("/lists/$list2Id").header("Authorization", "Bearer: $user1Auth"))
            .andExpect(status().is2xxSuccessful)

        // now it should be gone
        mvc.perform(get("/lists").header("Authorization", "Bearer: $user1Auth"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))
    }

    private fun createUserAndLogin(): String {
        val user = UUID.randomUUID().toString()

        mvc.perform(
            post("/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$user" }""")
        )
            .andExpect(status().`is`(204))

        val result = mvc.perform(
            post("/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$user" }""")
        )
            .andExpect(status().is2xxSuccessful).andReturn().response

        return JsonPath.read(result.contentAsString, "$.authorization")
    }
}
