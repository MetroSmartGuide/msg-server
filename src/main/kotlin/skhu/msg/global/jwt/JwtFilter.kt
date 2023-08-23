package skhu.msg.global.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

class JwtFilter (
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<Any, Any>,
): GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val accessToken = tokenProvider.resolveToken(request as HttpServletRequest)

        if (request.requestURI == "/api/v1/auth/refresh") {
            chain.doFilter(request, response)
            return
        }

        if (StringUtils.hasText(accessToken)) {
            if (tokenProvider.validateToken(accessToken!!)) {
                if (redisTemplate.opsForValue().get(accessToken) == null) {
                    val authentication: Authentication = tokenProvider.getAuthentication(accessToken)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }

        chain.doFilter(request, response)
    }

}