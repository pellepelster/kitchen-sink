package io.pelle.kitchensink.app

import io.pelle.kitchensink.model.GroupsRepository
import io.pelle.kitchensink.model.TodoItemsRepository
import io.pelle.kitchensink.model.UsersRepository

class TodoService(
    private val groupsRepository: GroupsRepository,
    private val usersRepository: UsersRepository,
    private val todoItemsRepository: TodoItemsRepository
) {

    fun totalOpenTodos(name: String) =
        groupsRepository.forName(name)?.let { group ->
            usersRepository.forGroup(group.id).sumOf {
                todoItemsRepository.forUser(it.id).filter { !it.done }.size
            }
        }
}