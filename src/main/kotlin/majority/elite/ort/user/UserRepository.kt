package majority.elite.ort.user

import majority.elite.ort.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
  fun findByOauthId(oauthId: String): UserEntity?
}
