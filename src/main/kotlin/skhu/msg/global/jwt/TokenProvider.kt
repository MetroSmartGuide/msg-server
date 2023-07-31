package skhu.msg.global.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import skhu.msg.domain.auth.dto.response.ResponseToken
import java.security.Key
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider(
    @Value("\${jwt.secret}") secretKey: String?,
    @Value("\${jwt.access-token-validity-in-milliseconds}") accessTokenValidityTime: Long,
) {

    private val key: Key
    private val accessTokenValidityTime: Long

    init {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
        this.accessTokenValidityTime = accessTokenValidityTime
    }

    fun createToken(email: String?): ResponseToken {
        val now = Date()
        val tokenExpiredTime = Date(now.time + accessTokenValidityTime)

        val accessToken = Jwts.builder()
            .setSubject(email)
            .claim("role", "ROLE_MEMBER")
            .setIssuedAt(now)
            .setExpiration(tokenExpiredTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return ResponseToken(accessToken)
    }

    fun getAuthentication(accessToken: String): UsernamePasswordAuthenticationToken {
        val claims = parseClaims(accessToken)

        if (claims["role"] == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        val authorities: Collection<GrantedAuthority?> =
            Arrays.stream(claims["role"].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
                .map { SimpleGrantedAuthority(it) }
                .collect(Collectors.toList())

        val principal: UserDetails = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken: String? = request.getHeader("Authorization")

        return if (StringUtils.hasText(bearerToken) && bearerToken?.startsWith("Bearer ") == true) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            print(e.message)
        }
        return false
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

}