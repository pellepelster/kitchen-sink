package io.pelle.kitchensink.model

public class ModelTestBed {
    val todoItemsRepository = TodoItemsRepository()
    val usersRepository = UsersRepository(todoItemsRepository)
    val groupsRepository = GroupsRepository(usersRepository)

    data class TestBed(val group: Group, val user: User, val todoItem: TodoItem)

    fun createUserWithOpenTodoItem(): TestBed {
        val group = groupsRepository.create("group1")
        val user = usersRepository.create("user1", group)
        val todoItem = todoItemsRepository.create("item1", false, user)

        return TestBed(group, user, todoItem)
    }

    fun createUserWithDoneTodoItem(): TestBed {
        val group = groupsRepository.create("group2")
        val user = usersRepository.create("user2", group)
        val todoItem = todoItemsRepository.create("item2", true, user)

        return TestBed(group, user, todoItem)
    }
}