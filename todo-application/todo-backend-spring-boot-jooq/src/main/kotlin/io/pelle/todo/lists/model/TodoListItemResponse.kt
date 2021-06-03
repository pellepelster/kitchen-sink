package io.pelle.todo.lists.model

import java.util.UUID

data class TodoListItemResponse(val id: UUID, val name: String, val done: Boolean)
