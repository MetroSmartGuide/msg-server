package skhu.msg.domain.member.app

import skhu.msg.domain.member.dto.response.ResponseMember
import java.security.Principal

interface MemberService {

    fun getMyInfo(principal: Principal): ResponseMember

    fun getMembers(): List<ResponseMember>

}