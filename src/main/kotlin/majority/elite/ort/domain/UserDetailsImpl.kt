package majority.elite.ort.domain

import jakarta.persistence.*
import kotlin.reflect.full.memberProperties
import majority.elite.ort.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

/*
* Spring Security에 활용할 유저 정보
* 차후 로그인 방식이 추가된다면 해당 방식에서 사용할 유저 정보 클래스를 본 클래스에 상속
*/
class UserDetailsImpl(
  val id: Long,
  val oauthId: String?,
  val oauthType: OAuthType?,
  val role: UserAuthority,
  val nickname: String?,
  val mail: String?,
  val tel: String?,
) : UserDetails, OAuth2User {
  // static 함수 구현
  companion object {
    fun fromUserEntity(userEntiity: UserEntity): UserDetailsImpl {
      return UserDetailsImpl(
        userEntiity.id,
        userEntiity.oauthId,
        userEntiity.oauthType,
        userEntiity.role,
        userEntiity.nickname,
        userEntiity.mail,
        userEntiity.tel,
      )
    }
  }

  override fun getName(): String {
    return "$id"
  }

  override fun getAttributes(): Map<String, Any?> {
    return this::class.memberProperties.associate { it.name to it.getter.call(this) }
  }

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return AuthorityUtils.createAuthorityList("ROLE_$role")
  }

  override fun getPassword(): String? {
    return null
  }

  override fun getUsername(): String {
    return this.name
  }
}
