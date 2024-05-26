package io.pelle.kitchensink.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class UsersRepositoryTest {

    @Test
    fun testCanNotDeleteUserWithTodoItems() {
        val todoItemsRepository = TodoItemsRepository()
        val usersRepository = UsersRepository(todoItemsRepository)
        val groupsRepository = GroupsRepository(usersRepository)

        val group1 = groupsRepository.create("group1")
        usersRepository.create("user1", group1)

        val user1 = usersRepository.create("user1", group1)
        todoItemsRepository.create("todoItem1", false, user1)

        shouldThrow<RuntimeException> {
            usersRepository.delete(user1.id)
        }.apply {
            message shouldContain "has todo items"
        }
    }

    @Test
    fun testCanDeleteUserWithoutItems() {
        val todoItemsRepository = TodoItemsRepository()
        val usersRepository = UsersRepository(todoItemsRepository)
        val groupsRepository = GroupsRepository(usersRepository)

        val group1 = groupsRepository.create("group1")
        usersRepository.create("user1", group1)

        val user1 = usersRepository.create("user1", group1)

        usersRepository.delete(user1.id) shouldBe true
    }
}
