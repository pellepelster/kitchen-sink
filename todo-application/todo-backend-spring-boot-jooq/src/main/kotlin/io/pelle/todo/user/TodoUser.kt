package io.pelle.todo.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class TodoUser(val id: UUID, val email: String) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return arrayListOf()
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
