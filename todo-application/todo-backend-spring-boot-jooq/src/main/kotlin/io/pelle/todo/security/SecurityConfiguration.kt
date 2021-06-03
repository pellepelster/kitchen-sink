package io.pelle.todo.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
internal class SecurityConfiguration(val provider: TokenAuthenticationProvider?) : WebSecurityConfigurerAdapter() {

    companion object {
        private val PUBLIC_URLS: RequestMatcher = OrRequestMatcher(
            AntPathRequestMatcher("/api/v1/users/public/**"),
            AntPathRequestMatcher("/api-docs/**"),
            AntPathRequestMatcher("/swagger-ui.html"),
            AntPathRequestMatcher("/swagger-ui/**")
        )

        private val PROTECTED_URLS: RequestMatcher = NegatedRequestMatcher(PUBLIC_URLS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(provider)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().requestMatchers(PUBLIC_URLS)
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/api/**")
                    .allowedMethods("GET", "POST", "DELETE", "PATCH")
            }
        }
    }

    override fun configure(http: HttpSecurity) {
        http
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
            .and()
            .authenticationProvider(provider)
            .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter::class.java)
            .authorizeRequests()
            .requestMatchers(PROTECTED_URLS)
            .authenticated()
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable()
    }

    @Bean
    fun restAuthenticationFilter(): TokenAuthenticationFilter {
        val filter = TokenAuthenticationFilter(PROTECTED_URLS)
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler(successHandler())
        return filter
    }

    @Bean
    fun successHandler(): SimpleUrlAuthenticationSuccessHandler {
        val successHandler = SimpleUrlAuthenticationSuccessHandler()
        successHandler.setRedirectStrategy(NoRedirectStrategy())
        return successHandler
    }

    @Bean
    fun forbiddenEntryPoint(): AuthenticationEntryPoint {
        return HttpStatusEntryPoint(HttpStatus.FORBIDDEN)
    }
}
