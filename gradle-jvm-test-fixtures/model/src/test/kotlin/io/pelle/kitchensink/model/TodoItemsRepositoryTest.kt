package io.pelle.kitchensink.model

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TodoItemsRepositoryTest {
    @Test
    fun testListTodoItems() {
        val todoItemsRepository = TodoItemsRepository()
        val usersRepository = UsersRepository(todoItemsRepository)
        val groupsRepository = GroupsRepository(usersRepository)

        val group1 = groupsRepository.create("group1")
        val user1 = usersRepository.create("user1", group1)

        todoItemsRepository.list() shouldBe emptyList()
        todoItemsRepository.create("item1", false, user1)

        assertSoftly(todoItemsRepository.list()) {
            it shouldHaveSize 1
            it[0].name shouldBe "item1"
        }
    }
}
