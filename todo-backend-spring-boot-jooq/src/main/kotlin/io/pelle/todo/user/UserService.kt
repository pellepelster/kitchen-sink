package io.pelle.todo.user

import io.pelle.todo.db.generated.Tables
import io.pelle.todo.db.generated.tables.records.UsersRecord
import org.jooq.DSLContext
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(val passwordEncoder: PasswordEncoder, val dsl: DSLContext) {

    fun login(email: String, password: String): UUID? {

        val user = dsl.selectFrom(Tables.USERS).where(Tables.USERS.EMAIL.eq(email)).fetchOne()

        if (user != null && passwordEncoder.matches(password, user.password)) {
            val tokenId = UUID.randomUUID()

            val token = dsl.newRecord(Tables.USERS_TOKENS)
            token.userId = user.id
            token.id = tokenId
            token.store()

            return tokenId
        }

        return null
    }

    fun create(user: UsersRecord) {
        val record = dsl.newRecord(Tables.USERS)
        record.id = user.id
        record.password = passwordEncoder.encode(user.password)
        record.email = user.email
        record.store()
    }

    fun hasUser(email: String): Boolean {
        return dsl.selectFrom(Tables.USERS).where(Tables.USERS.EMAIL.eq(email)).fetchOne() != null
    }
}
