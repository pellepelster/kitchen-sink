package io.pelle.kitchensink.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class GroupsRepositoryTest {

    @Test
    fun testCanNotDeleteNonEmptyGroup() {
        val todoItemsRepository = TodoItemsRepository()
        val usersRepository = UsersRepository(todoItemsRepository)
        val groupsRepository = GroupsRepository(usersRepository)

        val group1 = groupsRepository.create("group1")
        usersRepository.create("user1", group1)

        shouldThrow<RuntimeException> {
            groupsRepository.delete(group1.id)
        }.apply {
            message shouldContain "is not empty"
        }
    }

    @Test
    fun testCanDeleteEmptyGroup() {
        val todoItemsRepository = TodoItemsRepository()
        val usersRepository = UsersRepository(todoItemsRepository)
        val groupsRepository = GroupsRepository(usersRepository)

        val group1 = groupsRepository.create("group1")
        groupsRepository.delete(group1.id) shouldBe true
    }
}
