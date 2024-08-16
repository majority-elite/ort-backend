package majority.elite.ort.entity

import jakarta.persistence.*
import lombok.Builder.Default
import majority.elite.ort.domain.OAuthType
import majority.elite.ort.domain.UserAuthority

@Entity
@Table(name = "account")
class UserEntity(
  @Column(name = "oauth_id", nullable = true) val oauthId: Long?,
  @Enumerated(EnumType.STRING)
  @Column(name = "oauth_type", nullable = true)
  val oauthType: OAuthType?,
) : EntityTimestamp() {
  @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0
  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Default
  var role: UserAuthority = UserAuthority.USER
  @Column(name = "nickname", nullable = true) var nickname: String? = null
  @Column(name = "mail", nullable = true) var mail: String? = null
  @Column(name = "tel", nullable = true) var tel: String? = null
}
