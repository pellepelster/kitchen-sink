package io.pelle.kitchensink.model

import java.util.*

data class TodoItem(val id: UUID, val name: String, val user: User, val done: Boolean = false)

class TodoItemsRepository {

    private val todoItems = mutableListOf<TodoItem>()

    fun create(name: String, done: Boolean, user: User) =
        TodoItem(id = UUID.randomUUID(), name, user, done).also {
            todoItems.add(it)
        }

    fun forUser(id: UUID) = todoItems.filter { it.user.id == id }

    fun list() = todoItems.toList()
}
