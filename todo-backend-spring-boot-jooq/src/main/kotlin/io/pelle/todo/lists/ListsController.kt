package io.pelle.todo.lists

import io.pelle.todo.db.generated.Tables.LISTS
import io.pelle.todo.db.generated.Tables.LISTS_ITEMS
import io.pelle.todo.lists.model.TodoListCreateRequest
import io.pelle.todo.lists.model.TodoListItemCreateRequest
import io.pelle.todo.lists.model.TodoListItemResponse
import io.pelle.todo.lists.model.TodoListItemUpdateRequest
import io.pelle.todo.lists.model.TodoListResponse
import io.pelle.todo.user.TodoUser
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.jooq.DSLContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/lists", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Lists", description = "Todo Lists API")
class ListsController(val dsl: DSLContext) {

    @GetMapping
    fun list(@Parameter(hidden = true) authentication: Authentication): List<TodoListResponse> {
        return dsl.selectFrom(LISTS).where(LISTS.USER_ID.eq(ensureUserId(authentication))).fetch().map { TodoListResponse(it.id, it.name, null) }
    }

    @PostMapping
    fun create(
        @RequestBody request: TodoListCreateRequest,
        @RequestHeader("Authorization") auth: String,
        @Parameter(hidden = true) authentication: Authentication
    ): ResponseEntity<TodoListResponse> {
        val id = UUID.randomUUID()

        val list = dsl.newRecord(LISTS)
        list.id = id
        list.name = request.name
        list.userId = ensureUserId(authentication)
        list.store()

        return listResponseWithStatus(authentication, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<Void> {
        checkExistenceAndAccess<Void>(id, authentication)?.let { return it }

        dsl.deleteFrom(LISTS).where(LISTS.ID.eq(id).and(LISTS.USER_ID.eq(ensureUserId(authentication)))).execute()
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<TodoListResponse> {
        checkExistenceAndAccess<TodoListResponse>(id, authentication)?.let { return it }

        return listResponseWithStatus(authentication, HttpStatus.OK)
    }

    @PostMapping("/{id}")
    fun createItem(
        @PathVariable id: UUID,
        @RequestBody request: TodoListItemCreateRequest,
        @Parameter(hidden = true) authentication: Authentication
    ): ResponseEntity<TodoListResponse> {
        checkExistenceAndAccess<TodoListResponse>(id, authentication)?.let { return it }

        val item = dsl.newRecord(LISTS_ITEMS)
        item.id = UUID.randomUUID()
        item.name = request.name
        item.listId = id
        item.store()

        return listResponseWithStatus(authentication, HttpStatus.CREATED)
    }

    @DeleteMapping("/{listId}/items/{itemId}")
    fun deleteItem(
        @PathVariable listId: UUID,
        @PathVariable itemId: UUID,
        @Parameter(hidden = true) authentication: Authentication
    ): ResponseEntity<TodoListResponse> {

        dsl.deleteFrom(LISTS_ITEMS).where(LISTS_ITEMS.ID.eq(itemId)).execute()

        return listResponseWithStatus(authentication, HttpStatus.OK)
    }

    @PatchMapping("/{listId}/items/{itemId}")
    fun updateItem(
        @PathVariable listId: UUID,
        @PathVariable itemId: UUID,
        @RequestBody request: TodoListItemUpdateRequest,
        @Parameter(hidden = true) authentication: Authentication
    ): ResponseEntity<TodoListResponse> {

        checkExistenceAndAccess<TodoListResponse>(listId, authentication)?.let { return it }

        request.name?.let {
            dsl.update(LISTS_ITEMS).set(LISTS_ITEMS.NAME, it).where(LISTS_ITEMS.ID.eq(itemId)).execute()
        }
        request.done?.let {
            dsl.update(LISTS_ITEMS).set(LISTS_ITEMS.DONE, it).where(LISTS_ITEMS.ID.eq(itemId)).execute()
        }

        return listResponseWithStatus(authentication, HttpStatus.OK)
    }

    private fun <T : Any> checkExistenceAndAccess(
        id: UUID,
        authentication: Authentication
    ): ResponseEntity<T>? {

        val lists = dsl.selectFrom(LISTS).where(LISTS.ID.eq(id)).fetch()

        if (lists.isEmpty()) {
            return ResponseEntity.notFound().build()
        }

        if (!lists.all { it.userId == ensureUserId(authentication) }) {
            return ResponseEntity.status(403).build()
        }

        return null
    }

    private fun ensureUserId(authentication: Authentication): UUID {
        if (authentication.principal is TodoUser) {
            return (authentication.principal as TodoUser).id
        }

        throw RuntimeException("unexpected authentication principal")
    }

    private fun listResponseWithStatus(
        authentication: Authentication,
        status: HttpStatus
    ): ResponseEntity<TodoListResponse> {

        val result = dsl.select().from(LISTS.leftJoin(LISTS_ITEMS).on(LISTS.ID.eq(LISTS_ITEMS.LIST_ID))).where(LISTS.USER_ID.eq(ensureUserId(authentication))).orderBy(LISTS_ITEMS.NAME).fetchGroups({ it.into(LISTS) }, { it.into(LISTS_ITEMS) })
        val response = result.map {
            TodoListResponse(
                it.key.id,
                it.key.name,
                it.value.filter { item -> item.get(LISTS_ITEMS.ID) != null }.map { item -> TodoListItemResponse(item.get(LISTS_ITEMS.ID), item.get(LISTS_ITEMS.NAME), item.get(LISTS_ITEMS.DONE)) }
            )
        }

        return ResponseEntity<TodoListResponse>(response.first(), status)
    }
}
