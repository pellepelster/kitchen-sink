package io.pelle.kitchensink.model

import java.util.*

data class Group(val id: UUID, val name: String)

class GroupsRepository(private val usersRepository: UsersRepository) {

    private val groups = mutableListOf<Group>()

    fun create(name: String) = Group(id = UUID.randomUUID(), name = name).also {
        groups.add(it)
    }

    fun delete(id: UUID): Boolean {
        if (usersRepository.forGroup(id).isNotEmpty()) {
            throw RuntimeException("group with id '$id' is not empty")
        }

        return groups.removeIf { it.id == id }
    }

    fun forName(name: String) = groups.singleOrNull { it.name == name }
}
