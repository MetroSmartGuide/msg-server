package skhu.msg.domain.auth.app.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.auth.app.AuthService
import skhu.msg.domain.auth.dto.request.RequestLogin
import skhu.msg.domain.auth.dto.response.ResponseToken
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.entity.Member
import skhu.msg.global.jwt.TokenProvider

@Service
class AuthServiceImpl (
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider,
): AuthService {

    @Transactional
    override fun joinOrLogin(requestLogin: RequestLogin): ResponseToken {
        val memberEmail = requestLogin.email
        val memberNickname = requestLogin.nickname
        val memberUid = requestLogin.uid

        if (!memberRepository.existsByEmail(memberEmail)) {
            memberRepository.save(
                Member.create(
                    email = memberEmail,
                    nickname = memberNickname,
                    uid = memberUid,
                ))
        }

        return tokenProvider.createToken(memberEmail)
    }

}