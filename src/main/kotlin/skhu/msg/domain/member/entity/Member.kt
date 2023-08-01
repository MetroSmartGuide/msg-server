package skhu.msg.domain.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "members")
class Member(
    @Id
    var id: String? = null,
    var email: String,
    var nickname: String? = null,
    var uid: String? = null,
) {

    companion object {
        fun create(
            id: String? = null,
            email: String,
            nickname: String? = null,
            uid: String? = null,
        ): Member {
            if (id == null) {
                throw IllegalArgumentException("Member.of: arguments must not be null")
            }

            return Member(
                id = id,
                email = email,
                nickname = nickname,
                uid = uid,
            )
        }
    }

}