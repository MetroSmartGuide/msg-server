package skhu.msg.domain.auth.app.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.auth.app.AuthService
import skhu.msg.domain.auth.dto.request.RequestLogin
import skhu.msg.domain.auth.dto.response.ResponseToken
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.entity.Member
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import skhu.msg.global.jwt.TokenProvider

@Service
class AuthServiceImpl(
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider,
) : AuthService {

    @Transactional
    override fun joinOrLogin(requestLogin: RequestLogin): ResponseToken {
        return try {
            with(requestLogin) {
                if (!memberRepository.existsByEmail(email)) {
                    val newMember = Member.create(email, nickname, uid)
                    memberRepository.save(newMember)
                }
                tokenProvider.createToken(email)
            }
        } catch (ex: Exception) {
            throw GlobalException(ErrorCode.INVALID_LOGIN_INPUT)
        }
    }

}