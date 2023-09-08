package skhu.msg.domain.auth.app.impl

import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.auth.app.AuthService
import skhu.msg.domain.auth.dto.request.RequestLogin
import skhu.msg.domain.auth.dto.request.RequestRefresh
import skhu.msg.domain.auth.dto.response.ResponseToken
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.dao.PreferencesRepository
import skhu.msg.domain.member.entity.Member
import skhu.msg.domain.member.entity.Preferences
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import skhu.msg.global.jwt.TokenProvider
import java.util.concurrent.TimeUnit

@Service
class AuthServiceImpl(
    private val memberRepository: MemberRepository,
    private val preferencesRepository: PreferencesRepository,
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<Any, Any>,
): AuthService {

    @Transactional
    override fun joinOrLogin(requestLogin: RequestLogin): ResponseToken {
        return try {
            val email = requestLogin.email ?: throw GlobalException(ErrorCode.INVALID_EMAIL)

            if (existMemberByEmail(email)) {
                val responseToken = tokenProvider.createToken(email)
                storeRefreshTokenInRedis(responseToken.refreshToken)
                responseToken
            } else {
                val newMember = createNewMember(email, requestLogin.nickname, requestLogin.uid)
                createDefaultPreferences(email)

                val responseToken = tokenProvider.createToken(newMember.email)
                storeRefreshTokenInRedis(responseToken.refreshToken)
                responseToken
            }
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.LOGIN_FAILED)
        }
    }

    override fun refreshAccessToken(request: HttpServletRequest, requestRefresh: RequestRefresh): ResponseToken {
        return try {
            val refreshToken = requestRefresh.refreshToken!!

            validateRefreshToken(refreshToken)

            tokenProvider.refreshAccessTokenByRefreshToken(request, refreshToken)
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.REFRESH_TOKEN_FAILED)
        }
    }

    override fun logout(request: HttpServletRequest, requestRefresh: RequestRefresh) {
        try {
            val accessToken = tokenProvider.resolveToken(request)!!
            val refreshToken = requestRefresh.refreshToken!!

            storeLoggedOutAccessTokenInRedis(accessToken)

            validateRefreshToken(refreshToken)

            redisTemplate.delete(refreshToken)
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.LOGOUT_FAILED)
        }
    }

    private fun existMemberByEmail(email: String): Boolean =
        memberRepository.existsByEmail(email)

    private fun createNewMember(email: String, nickname: String?, uid: String?): Member =
        memberRepository.save(Member.create(email, nickname, uid))

    private fun createDefaultPreferences(email: String): Preferences =
        preferencesRepository.save(Preferences.create(email, 1, 1, 1))

    private fun storeRefreshTokenInRedis(refreshToken: String) {
        val expiration: Long = tokenProvider.getExpirationTime(refreshToken)

        redisTemplate.opsForValue()
            .set(refreshToken, "refreshToken", expiration, TimeUnit.MILLISECONDS)
    }

    private fun validateRefreshToken(refreshToken: String) {
        redisTemplate.opsForValue().get(refreshToken)
            ?: throw GlobalException(ErrorCode.INVALID_JWT)
    }

    private fun storeLoggedOutAccessTokenInRedis(accessToken: String) {
        val expiration: Long = tokenProvider.getExpirationTime(accessToken)

        redisTemplate.opsForValue()
            .set(accessToken, "loggedOutAccessToken", expiration, TimeUnit.MILLISECONDS)
    }

}