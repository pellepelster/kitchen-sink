package io.pelle.todo.security

import org.springframework.security.web.RedirectStrategy
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class NoRedirectStrategy : RedirectStrategy {
    override fun sendRedirect(request: HttpServletRequest, response: HttpServletResponse, url: String) {
    }
}
