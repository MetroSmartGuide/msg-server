package skhu.msg.domain.member.app.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.member.app.PreferencesService
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.dao.PreferencesRepository
import skhu.msg.domain.member.dto.request.RequestUpdatePreferences
import skhu.msg.domain.member.dto.response.ResponseMemberPreferences
import skhu.msg.domain.member.entity.Member
import skhu.msg.domain.member.entity.Preferences
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import java.security.Principal

@Service
class PreferencesServiceImpl(
    private val preferencesRepository: PreferencesRepository,
    private val memberRepository: MemberRepository,
): PreferencesService {

    @Transactional
    override fun setUpPreferences(principal: Principal?, requestUpdatePreferences: RequestUpdatePreferences) {
        principal ?: throw GlobalException(ErrorCode.INVALID_JWT)
        val memberEmail = principal.name
        val newFastExitScore = requestUpdatePreferences.fastExitScore!!
        val newCoolingCarScore = requestUpdatePreferences.coolingCarScore!!
        val newGettingSeatScore = requestUpdatePreferences.gettingSeatScore!!

        val preferences = preferencesRepository.findByMemberEmail(memberEmail)
            ?: run {
                val newPreferences = Preferences.create(
                    memberEmail = memberEmail,
                    fastExitScore = newFastExitScore,
                    coolingCarScore = newCoolingCarScore,
                    gettingSeatScore = newGettingSeatScore
                )
                preferencesRepository.save(newPreferences)
            }

        preferences.updateExitScore(newFastExitScore)
        preferences.updateCoolingScore(newCoolingCarScore)
        preferences.updateSeatScore(newGettingSeatScore)
        preferencesRepository.save(preferences)
    }

    @Transactional
    override fun updateFastExitScore(principal: Principal?, fastExitScore: Int) {
        principal ?: throw GlobalException(ErrorCode.INVALID_JWT)
        val memberEmail = principal.name
        val preferences = getPreferencesByEmail(memberEmail)

        preferences.updateExitScore(fastExitScore)
        preferencesRepository.save(preferences)
    }

    @Transactional
    override fun updateCoolingCarScore(principal: Principal?, coolingCarScore: Int) {
        principal ?: throw GlobalException(ErrorCode.INVALID_JWT)
        val memberEmail = principal.name
        val preferences = getPreferencesByEmail(memberEmail)

        preferences.updateCoolingScore(coolingCarScore)
        preferencesRepository.save(preferences)
    }

    @Transactional
    override fun updateGettingSeatScore(principal: Principal?, gettingSeatScore: Int) {
        principal ?: throw GlobalException(ErrorCode.INVALID_JWT)
        val memberEmail = principal.name
        val preferences = getPreferencesByEmail(memberEmail)

        preferences.updateSeatScore(gettingSeatScore)
        preferencesRepository.save(preferences)
    }

    @Transactional(readOnly = true)
    override fun getPreferences(principal: Principal?): ResponseMemberPreferences {
        principal ?: throw GlobalException(ErrorCode.INVALID_JWT)
        val memberEmail = principal.name
        val member = getMemberByEmail(memberEmail)
        val preferences = getPreferencesByEmail(memberEmail)

        validateMember(member)
        validatePreferences(preferences)

        return mapToResponseMemberPreferences(member, preferences)
    }

    private fun getMemberByEmail(memberEmail: String) =
        memberRepository.findByEmail(memberEmail)
            ?: throw GlobalException(ErrorCode.NOT_FOUND_MEMBER)

    private fun getPreferencesByEmail(memberEmail: String) =
        preferencesRepository.findByMemberEmail(memberEmail)
            ?: throw GlobalException(ErrorCode.NOT_FOUND_PREFERENCES)

    private fun validateMember(member: Member) {
        checkNotNull(member.id) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(member.nickname) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(member.uid) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
    }

    private fun validatePreferences(preferences: Preferences) {
        checkNotNull(preferences.fastExitScore) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(preferences.coolingCarScore) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(preferences.gettingSeatScore) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
    }

    private fun mapToResponseMemberPreferences(member: Member, preferences: Preferences) =
        ResponseMemberPreferences.create(
            email = preferences.memberEmail,
            nickname = member.nickname!!,
            exit = preferences.fastExitScore!!,
            cooling = preferences.coolingCarScore!!,
            seat = preferences.gettingSeatScore!!,
        )

}