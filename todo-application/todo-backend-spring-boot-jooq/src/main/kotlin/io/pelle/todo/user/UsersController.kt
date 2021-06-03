package io.pelle.todo.user

import io.pelle.todo.db.generated.tables.records.UsersRecord
import io.pelle.todo.security.TokenAuthenticationProvider.Companion.ensureUser
import io.pelle.todo.user.model.UserLoginRequest
import io.pelle.todo.user.model.UserLoginResponse
import io.pelle.todo.user.model.UserRegistrationRequest
import io.pelle.todo.user.model.UserRegistrationResponse
import io.pelle.todo.user.model.WhoAmiResponse
import io.pelle.todo.util.ErrorMessage
import io.pelle.todo.util.ErrorResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
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

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = UserLoginResponse::class))
                ]
            ),
            ApiResponse(
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = ErrorResponse::class))
                ]
            ),
        ]
    )
    @PostMapping("public/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<out Any> {

        if (request.email.isEmpty()) {
            val errors = listOf(ErrorMessage("email", "users.register.email.mandatory"))
            return ResponseEntity.status(400).body(ErrorResponse(errors))
        }

        if (request.password.isEmpty()) {
            val errors = listOf(ErrorMessage("password", "users.register.password.mandatory"))
            return ResponseEntity.status(400).body(ErrorResponse(errors))
        }

        if (userService.hasUser(request.email)) {
            val errors = listOf(ErrorMessage("email", "users.register.email.duplicate"))
            return ResponseEntity.status(400).body(ErrorResponse(errors))
        }

        userService.create(UsersRecord(UUID.randomUUID(), request.email, request.password))
        return ResponseEntity<UserRegistrationResponse>(HttpStatus.NO_CONTENT)
    }

    @PostMapping("public/login")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = UserLoginResponse::class))
                ]
            ),
            ApiResponse(
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = ErrorResponse::class))
                ]
            ),
        ]
    )
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<out Any> {

        if (request.email.isEmpty()) {
            val errors = listOf(ErrorMessage("email", "users.login.email.mandatory"))
            return ResponseEntity.status(400).body(ErrorResponse(errors))
        }

        if (request.password.isEmpty()) {
            val errors = listOf(ErrorMessage("password", "users.login.password.mandatory"))
            return ResponseEntity.status(400).body(ErrorResponse(errors))
        }

        val token = userService.login(request.email, request.password)

        if (token != null) {
            return ResponseEntity.ok(UserLoginResponse(token))
        }

        return ResponseEntity.status(401).build()
    }

    @GetMapping("whoami")
    fun whoami(authentication: Authentication): ResponseEntity<WhoAmiResponse> {
        return ResponseEntity.ok(WhoAmiResponse(ensureUser(authentication).email))
    }
}
