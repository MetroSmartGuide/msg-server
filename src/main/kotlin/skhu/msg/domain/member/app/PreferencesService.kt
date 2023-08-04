package skhu.msg.domain.member.app

import skhu.msg.domain.member.dto.request.RequestUpdatePreferences
import skhu.msg.domain.member.dto.response.ResponseMemberPreferences
import java.security.Principal

interface PreferencesService {

    fun setUpPreferences(requestUpdatePreferences: RequestUpdatePreferences)

    fun getPreferences(principal: Principal): ResponseMemberPreferences

}