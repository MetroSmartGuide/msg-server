package skhu.msg.domain.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException

@Document(collection = "member-preferences")
class Preferences(
    @Id
    var id: String? = null,
    var memberEmail: String,
    var fastExitScore: Int? = null,
    var coolingCarScore: Int? = null,
    var gettingSeatScore: Int? = null,
) {

    companion object {
        fun create (
            memberEmail: String,
            fastExitScore: Int? = null,
            coolingCarScore: Int? = null,
            gettingSeatScore: Int? = null,
        ): Preferences {

            return Preferences(
                memberEmail = memberEmail,
                fastExitScore = fastExitScore,
                coolingCarScore = coolingCarScore,
                gettingSeatScore = gettingSeatScore,
            )
        }
    }

    fun updateExitScore(newExitScore: Int?) {
        if (newExitScore != null && (newExitScore < 0 || newExitScore > 5))
            throw GlobalException(ErrorCode.INVALID_UPDATE_VALUE)

        this.fastExitScore = newExitScore
    }

    fun updateCoolingScore(newCoolingScore: Int?) {
        if (newCoolingScore != null && (newCoolingScore < 0 || newCoolingScore > 5))
            throw GlobalException(ErrorCode.INVALID_UPDATE_VALUE)

        this.coolingCarScore = newCoolingScore
    }

    fun updateSeatScore(newSeatScore: Int?) {
        if (newSeatScore != null && (newSeatScore < 0 || newSeatScore > 5))
            throw GlobalException(ErrorCode.INVALID_UPDATE_VALUE)

        this.gettingSeatScore = newSeatScore
    }

}