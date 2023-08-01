package skhu.msg.domain.member.dao

import org.springframework.data.mongodb.repository.MongoRepository
import skhu.msg.domain.member.entity.Member

interface MemberRepository: MongoRepository<Member, String> {

    fun findByEmail(email: String): Member?

    fun existsByEmail(email: String): Boolean

}