package skhu.msg.domain.member.app.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.member.app.MemberService
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.dto.response.ResponseMember
import skhu.msg.domain.member.entity.Member
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import java.security.Principal

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository
): MemberService {

    @Transactional(readOnly = true)
    override fun getMyInfo(principal: Principal): ResponseMember {
        val memberEmail = principal.name
        val member = getMemberByEmail(memberEmail)

        validateMember(member)

        return mapToResponseMember(member)
    }

    @Transactional(readOnly = true)
    override fun getMembers(): List<ResponseMember> {
        return memberRepository.findAll().map {
                member -> validateMember(member)
            mapToResponseMember(member)
        }
    }

    private fun getMemberByEmail(memberEmail: String) =
        memberRepository.findByEmail(memberEmail)
            ?: throw GlobalException(ErrorCode.NOT_FOUND_MEMBER)

    private fun validateMember(member: Member) {
        checkNotNull(member.id) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(member.nickname) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(member.uid) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
    }

    private fun mapToResponseMember(member: Member) =
        ResponseMember.create(
            id = member.id!!,
            email = member.email,
            nickname = member.nickname!!,
            uid = member.uid!!,
        )

}