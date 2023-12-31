package skhu.msg.domain.member.app

import skhu.msg.domain.member.dto.request.RequestUpdatePreferences
import skhu.msg.domain.member.dto.response.ResponseMemberPreferences
import java.security.Principal

interface PreferencesService {

    fun setUpPreferences(principal: Principal?, requestUpdatePreferences: RequestUpdatePreferences)

    fun updateFastExitScore(principal: Principal?, fastExitScore: Int)

    fun updateCoolingCarScore(principal: Principal?, coolingCarScore: Int)

    fun updateGettingSeatScore(principal: Principal?, gettingSeatScore: Int)

    fun getPreferences(principal: Principal?): ResponseMemberPreferences

}