package majority.elite.ort.repository

import majority.elite.ort.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
  fun findByOauthId(oauthId: String): UserEntity?

  fun findByNickname(nickname: String): UserEntity?
}
