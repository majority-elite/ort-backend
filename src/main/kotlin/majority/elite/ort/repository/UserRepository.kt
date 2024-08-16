package majority.elite.ort.repository

import majority.elite.ort.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
  fun findByOauthId(oauthId: Long): UserEntity?

  fun findByNickname(nickname: String): UserEntity?
}
