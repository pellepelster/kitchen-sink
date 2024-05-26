package io.pelle.kitchensink.model

import java.util.*

data class User(val id: UUID, val name: String, val group: Group)

class UsersRepository(private val todoItemsRepository: TodoItemsRepository) {

    private val users = mutableListOf<User>()

    fun create(name: String, group: Group) = User(id = UUID.randomUUID(), name = name, group).also {
        users.add(it)
    }

    fun list() = users.toList()

    fun forGroup(id: UUID) = users.filter { it.group.id == id }

    fun delete(id: UUID): Boolean {
        if (todoItemsRepository.forUser(id).isNotEmpty()) {
            throw RuntimeException("user with id '$id' has todo items")
        }

        return users.removeIf { it.id == id }
    }
}
