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
import skhu.msg.domain.member.entity.Member
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import skhu.msg.global.jwt.TokenProvider
import java.util.concurrent.TimeUnit

@Service
class AuthServiceImpl(
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<String, Any>,
): AuthService {

    @Transactional
    override fun joinOrLogin(requestLogin: RequestLogin): ResponseToken {
        return try {
            val email = requestLogin.email ?: throw GlobalException(ErrorCode.INVALID_EMAIL)

            if (existMemberByEmail(email)) {
                val responseToken = tokenProvider.createToken(email)
                saveRefreshToken(responseToken.refreshToken)
                responseToken
            } else {
                val newMember = createNewMember(email, requestLogin.nickname, requestLogin.uid)
                val responseToken = tokenProvider.createToken(newMember.email)
                saveRefreshToken(responseToken.refreshToken)
                responseToken
            }
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR_LOGIN)
        }
    }

    override fun refreshAccessToken(request: HttpServletRequest, requestRefresh: RequestRefresh): ResponseToken {
        return try {
            val refreshToken = requestRefresh.refreshToken!!

            validateRefreshToken(refreshToken)

            tokenProvider.refreshAccessTokenByRefreshToken(request, refreshToken)
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.INVALID_JWT)
        }
    }

    private fun existMemberByEmail(email: String): Boolean =
        memberRepository.existsByEmail(email)

    private fun createNewMember(email: String, nickname: String?, uid: String?): Member =
        memberRepository.save(Member.create(email, nickname, uid))
    private fun saveRefreshToken(refreshToken: String) {
        val expiration: Long = tokenProvider.getExpirationTime(refreshToken)

        redisTemplate.opsForValue()
            .set(refreshToken, "refreshToken", expiration, TimeUnit.MILLISECONDS)
    }

    private fun validateRefreshToken(refreshToken: String) {
        redisTemplate.opsForValue().get(refreshToken)
            ?: throw GlobalException(ErrorCode.INVALID_JWT)
    }

}