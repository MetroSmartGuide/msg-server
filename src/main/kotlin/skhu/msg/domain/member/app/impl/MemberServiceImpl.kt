package skhu.msg.domain.member.app.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.member.app.MemberService
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.dto.response.ResponseMember
import java.security.Principal

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository
): MemberService {

    @Transactional(readOnly = true)
    override fun getMyInfo(principal: Principal): ResponseMember {
        val memberEmail = principal.name
        val member = getMemberByEmail(memberEmail)

        return ResponseMember.create(
            id = member.id,
            email = member.email,
            nickname = member.nickname,
            uid = member.uid
        )
    }

    @Transactional(readOnly = true)
    override fun getMembers(): List<ResponseMember> {
        return memberRepository.findAll().map {
                member -> ResponseMember.create(
                id = member.id,
                email = member.email,
                nickname = member.nickname,
                uid = member.uid
            )
        }
    }

    private fun getMemberByEmail(memberEmail: String) =
        memberRepository.findByEmail(memberEmail)
            ?: throw Exception("회원 정보를 찾을 수 없습니다.")

}