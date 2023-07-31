package skhu.msg.global.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

class JwtFilter (
    private val tokenProvider: TokenProvider
): GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = tokenProvider.resolveToken(request as HttpServletRequest)

        if (StringUtils.hasText(token)) {
            if (tokenProvider.validateToken(token)) {
                val authentication: Authentication = tokenProvider.getAuthentication(token!!)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        chain.doFilter(request, response)
    }

}