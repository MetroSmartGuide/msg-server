package skhu.msg.domain.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "member-preferences")
class Preferences(
    @Id
    var id: String? = null,
    var memberEmail: String,
    var exitScore: Int? = null,
    var coolingScore: Int? = null,
    var seatScore: Int? = null,
) {

    companion object {
        fun create (
            id: String? = null,
            memberEmail: String,
            exitScore: Int? = null,
            coolingScore: Int? = null,
            seatScore: Int? = null,
        ): Preferences {
            if (id == null) {
                throw IllegalArgumentException("Preferences.of: arguments must not be null")
            }

            return Preferences(
                id = id,
                memberEmail = memberEmail,
                exitScore = exitScore,
                coolingScore = coolingScore,
                seatScore = seatScore,
            )
        }
    }

    fun updateExitScore(newExitScore: Int?) {
        if (newExitScore != null && (newExitScore < 0 || newExitScore > 5)) {
            throw IllegalArgumentException("exitScore must be between 0 and 5")
        }
        this.exitScore = newExitScore
    }

    fun updateCoolingScore(newCoolingScore: Int?) {
        if (newCoolingScore != null && (newCoolingScore < 0 || newCoolingScore > 5)) {
            throw IllegalArgumentException("coolingScore must be between 0 and 5")
        }
        this.coolingScore = newCoolingScore
    }

    fun updateSeatScore(newSeatScore: Int?) {
        if (newSeatScore != null && (newSeatScore < 0 || newSeatScore > 5)) {
            throw IllegalArgumentException("seatScore must be between 0 and 5")
        }
        this.seatScore = newSeatScore
    }

}