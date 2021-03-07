package io.pelle.todo.user

import io.pelle.todo.db.generated.tables.records.UsersRecord
import io.pelle.todo.user.model.UserLoginRequest
import io.pelle.todo.user.model.UserLoginResponse
import io.pelle.todo.user.model.UserRegistrationRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Users", description = "User API")
class UsersController(
    val userService: UserService
) {

    @PostMapping("public/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<Void> {

        if (userService.hasUser(request.email)) {
            return ResponseEntity.status(409).build()
        }

        userService.create(UsersRecord(UUID.randomUUID(), request.email, request.password))
        return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }

    @PostMapping("public/login")
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<UserLoginResponse> {
        val token = userService.login(request.email, request.password)

        if (token != null) {
            return ResponseEntity.ok(UserLoginResponse(token))
        }

        return ResponseEntity.status(401).build()
    }
}
