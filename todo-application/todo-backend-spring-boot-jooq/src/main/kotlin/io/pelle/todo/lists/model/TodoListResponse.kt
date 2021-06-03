package io.pelle.todo.lists.model

import java.util.UUID

data class TodoListResponse(val id: UUID, val name: String, val items: List<TodoListItemResponse>?)
