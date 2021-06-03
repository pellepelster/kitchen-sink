package io.pelle.todo.security

import io.pelle.todo.db.generated.tables.Users.Companion.USERS
import io.pelle.todo.db.generated.tables.UsersTokens.Companion.USERS_TOKENS
import io.pelle.todo.user.TodoUser
import org.jooq.DSLContext
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenAuthenticationProvider(val dsl: DSLContext) : AbstractUserDetailsAuthenticationProvider() {

    companion object {
        fun ensureUserId(authentication: Authentication): UUID {
            return ensureUser(authentication).id
        }

        fun ensureUser(authentication: Authentication): TodoUser {
            if (authentication.principal is TodoUser) {
                return (authentication.principal as TodoUser)
            }

            throw RuntimeException("unexpected authentication principal")
        }
    }

    override fun additionalAuthenticationChecks(d: UserDetails, auth: UsernamePasswordAuthenticationToken) {
    }

    override fun retrieveUser(username: String, authentication: UsernamePasswordAuthenticationToken): UserDetails {

        val token: String = authentication.credentials as String
        val user = dsl.select().from(
            USERS.leftJoin(USERS_TOKENS).on(USERS.ID.eq(USERS_TOKENS.USER_ID))
                .where(USERS_TOKENS.ID.eq(UUID.fromString(token)))
        ).fetchOneInto(USERS)

        if (user != null) {
            return TodoUser(user.id!!, user.email!!)
        }

        throw UsernameNotFoundException("Cannot find user with authentication token=$token")
    }
}
