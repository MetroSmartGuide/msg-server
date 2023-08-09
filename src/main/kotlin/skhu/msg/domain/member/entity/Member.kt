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
            email: String,
            nickname: String? = null,
            uid: String? = null,
        ): Member = Member(
            email = email,
            nickname = nickname,
            uid = uid,
        )
    }

}