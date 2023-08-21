package skhu.msg.global.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import skhu.msg.domain.auth.dto.response.ResponseToken
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import java.security.Key
import java.util.*

@Component
class TokenProvider(
    @Value("\${jwt.secret}") secretKey: String?,
    @Value("\${jwt.access-token-validity-in-milliseconds}") accessTokenValidityTime: Long,
    @Value("\${jwt.refresh-token-validity-in-milliseconds}") refreshTokenValidityTime: Long,
) {

    private final val key: Key
    private final val accessTokenValidityTime: Long
    private final val refreshTokenValidityTime: Long

    init {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
        this.accessTokenValidityTime = accessTokenValidityTime
        this.refreshTokenValidityTime = refreshTokenValidityTime
    }

    fun createToken(email: String): ResponseToken {
        return try {
            val now = Date()

            val tokenExpiredTime = Date(now.time + accessTokenValidityTime)
            val accessToken = Jwts.builder()
                .setSubject(email)
                .claim("role", "ROLE_MEMBER")
                .setIssuedAt(now)
                .setExpiration(tokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()

            val refreshTokenExpiredTime = Date(now.time + refreshTokenValidityTime)
            val refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()

            ResponseToken.create(accessToken, refreshToken)
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR_JWT)
        }
    }

    fun refreshAccessTokenByRefreshToken(request: HttpServletRequest, refreshToken: String): ResponseToken {
        return try {
            val accessToken = resolveToken(request) ?: throw GlobalException(ErrorCode.INVALID_JWT)
            val now = Date()

            val tokenExpiredTime = Date(now.time + accessTokenValidityTime)
            val newAccessToken = Jwts.builder()
                .setSubject(parseClaims(accessToken).subject)
                .claim("role", "ROLE_MEMBER")
                .setIssuedAt(now)
                .setExpiration(tokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()

            ResponseToken.create(newAccessToken, refreshToken)
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR_JWT)
        }
    }

    fun getAuthentication(accessToken: String): UsernamePasswordAuthenticationToken {
        return try {
            val claims = parseClaims(accessToken)

            val role = claims["role"] as? String
                ?: throw GlobalException(ErrorCode.FORBIDDEN_TOKEN)

            val authorities = role.split(",").map { SimpleGrantedAuthority(it) }

            val principal: UserDetails = User(claims.subject, "", authorities)

            UsernamePasswordAuthenticationToken(principal, "", authorities)
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.FORBIDDEN_TOKEN)
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken: String? = request.getHeader("Authorization")

        return if (StringUtils.hasText(bearerToken) && bearerToken?.startsWith("Bearer ") == true) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String?): Boolean {
        return try {
            token?.let {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(it)
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    fun getExpirationTime(token: String): Long {
        val expirationTime = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.expiration.time
        val now = Date().time
        return expirationTime - now
    }

}