package io.pelle.todo

import com.jayway.jsonpath.JsonPath
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

    @Test
    fun testRegistration() {

        val user = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()

        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$password" }""")
        )
            .andExpect(status().`is`(204))

        mvc.perform(
            post("/api/v1/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$password" }""")
        )
            .andExpect(status().is2xxSuccessful)
    }

    @Test
    fun testInvalidRegistration() {

        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "", "password": "password" }""")
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("$.messages[0].code", `is`("users.register.email.mandatory")))
            .andExpect(jsonPath("$.messages[0].attribute", `is`("email")))

        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "email", "password": "" }""")
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("$.messages[0].code", `is`("users.register.password.mandatory")))
            .andExpect(jsonPath("$.messages[0].attribute", `is`("password")))
    }

    @Test
    fun testRegistrationDuplicateEmail() {

        val user = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()

        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$password" }""")
        )
            .andExpect(status().`is`(204))

        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$user", "password": "$password" }""")
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("$.messages[0].code", `is`("users.register.email.duplicate")))
    }

    @Test
    fun testInvalidLogin() {

        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "", "password": "password" }""")
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("$.messages[0].code", `is`("users.register.email.mandatory")))
            .andExpect(jsonPath("$.messages[0].attribute", `is`("email")))

        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "email", "password": "" }""")
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("$.messages[0].code", `is`("users.register.password.mandatory")))
            .andExpect(jsonPath("$.messages[0].attribute", `is`("password")))

        mvc.perform(
            post("/api/v1/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "YYY", "password": "XXX" }""")
        )
            .andExpect(status().`is`(401))

        mvc.perform(
            post("/api/v1/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: XXXXX")
                .content("""{ "email": "YYY", "password": "XXX" }""")
        )
            .andExpect(status().`is`(401))

        mvc.perform(
            post("/api/v1/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "")
                .content("""{ "email": "YYY", "password": "XXX" }""")
        )
            .andExpect(status().`is`(401))
    }

    @Test
    fun testUsersWhoAmI() {
        val email = UUID.randomUUID()
        val userAuth: String = createUserAndLogin(email)

        mvc.perform(
            get("/api/v1/users/whoami")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $userAuth")
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.email", `is`(email.toString())))
    }

    @Test
    fun testListWorkflow() {
        val user1Auth: String = createUserAndLogin()
        val user2Auth: String = createUserAndLogin()

        // initially there should no lists at all
        mvc.perform(
            get("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))

        // lets create a list
        val list1Name = UUID.randomUUID().toString()
        val list1Result = mvc.perform(
            post("/api/v1/lists")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "$list1Name" }""")
        )
            .andExpect(status().`is`(201))
            .andExpect(jsonPath("$.name", `is`(list1Name))).andReturn().response
        val list1Id: String = JsonPath.read(list1Result.contentAsString, "$.id")

        // now we expect exactly one list
        mvc.perform(
            get("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(1)))
            .andExpect(jsonPath("$[0].name", `is`(list1Name)))

        // user 2 should not see anything
        mvc.perform(
            get("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user2Auth")
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))

        // user 2 should not be able to delete it
        mvc.perform(
            delete("/api/v1/lists/$list1Id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user2Auth")
        )
            .andExpect(status().isForbidden)

        // try to delete non existing list
        mvc.perform(
            delete("/api/v1/lists/79543e6d-e074-40af-a491-10dd6b0d9b08")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().isNotFound)

        // and delete it immediately
        mvc.perform(
            delete("/api/v1/lists/$list1Id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().is2xxSuccessful)

        // now it should be gone
        mvc.perform(
            get("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))

        // create a new one
        val list2Name = UUID.randomUUID().toString()
        val response = mvc.perform(
            post("/api/v1/lists")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "$list2Name" }""")
        )
            .andExpect(status().`is`(201)).andReturn().response
        val list2Id: String = JsonPath.read(response.contentAsString, "$.id")

        // user 2 should not be able to add an item
        mvc.perform(
            post("/api/v1/lists/$list2Id")
                .header("Authorization", "Bearer: $user2Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "${UUID.randomUUID()}" }""")
        )
            .andExpect(status().`is`(403))

        // add to invalid list should fail
        mvc.perform(
            post("/api/v1/lists/bd02ef7b-9e13-4ccc-bc7c-b53eff1f4907")
                .header("Authorization", "Bearer: $user2Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "${UUID.randomUUID()}" }""")
        )
            .andExpect(status().`is`(404))

        // adding an item with an empty name should fail
        mvc.perform(
            post("/api/v1/lists/$list2Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "" }""")
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("$.messages[0].code", `is`("items.create.name.mandatory")))
            .andExpect(jsonPath("$.messages[0].attribute", `is`("name")))

        // and add an item
        val item1Name = "AAA"
        val item1AddResult = mvc.perform(
            post("/api/v1/lists/$list2Id")
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
            get("/api/v1/lists/$list2Id")
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
            patch("/api/v1/lists/$list2Id/items/$item1Id")
                .header("Authorization", "Bearer: $user2Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "name": "newnameforbidden" }""")
        )
            .andExpect(status().`is`(403))

        // update name of item1
        val item1NewName = "BBB"
        mvc.perform(
            patch("/api/v1/lists/$list2Id/items/$item1Id")
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
            patch("/api/v1/lists/$list2Id/items/$item1Id")
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
            post("/api/v1/lists/$list2Id")
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
            delete("/api/v1/lists/$list2Id/items/$item1Id")
                .header("Authorization", "Bearer: $user1Auth")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(200))
            .andExpect(jsonPath("$.items.length()", `is`(1)))
            .andExpect(jsonPath("$.items[0].name", `is`("AAA")))

        // delete list 2
        mvc.perform(
            delete("/api/v1/lists/$list2Id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().is2xxSuccessful)

        // now it should be gone
        mvc.perform(
            get("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer: $user1Auth")
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.length()", `is`(0)))
    }

    private fun createUserAndLogin(email: UUID = UUID.randomUUID()): String {
        mvc.perform(
            post("/api/v1/users/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$email", "password": "$email" }""")
        )
            .andExpect(status().`is`(204))

        val result = mvc.perform(
            post("/api/v1/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "$email", "password": "$email" }""")
        )
            .andExpect(status().is2xxSuccessful).andReturn().response

        return JsonPath.read(result.contentAsString, "$.authorization")
    }
}
