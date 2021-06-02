package io.pelle.todo.security

import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import java.util.UUID
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter(requiresAuth: RequestMatcher?) :
    AbstractAuthenticationProcessingFilter(requiresAuth) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        val authorization = request.getHeader("Authorization")

        if (authorization != null) {
            val token = StringUtils.removeStart(authorization, "$BEARER:").trim()

            try {
                UUID.fromString(token)
            } catch (e: Exception) {
                throw BadCredentialsException("Missing Authentication Token")
            }

            val auth: Authentication = UsernamePasswordAuthenticationToken(token, token)
            return authenticationManager.authenticate(auth)
        }

        throw BadCredentialsException("Missing Authentication Token")
    }

    companion object {
        private const val BEARER = "Bearer"
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        super.successfulAuthentication(request, response, chain, authResult)
        chain.doFilter(request, response)
    }
}
