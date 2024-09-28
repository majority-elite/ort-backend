package majority.elite.ort.user

import jakarta.persistence.*
import lombok.Builder.Default
import majority.elite.ort.oauth2.domain.OAuthType
import majority.elite.ort.auth.domain.UserAuthority
import majority.elite.ort.db.EntityTimestamp

@Entity
@Table(name = "account")
class UserEntity(
  @Column(name = "oauth_id", nullable = true) val oauthId: String?,
  @Enumerated(EnumType.STRING)
  @Column(name = "oauth_type", nullable = true)
  val oauthType: OAuthType?,
) : EntityTimestamp() {
  @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0
  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Default
  var role: UserAuthority = UserAuthority.USER
  @Column(name = "mail", nullable = true) var mail: String? = null
  @Column(name = "tel", nullable = true) var tel: String? = null
  @Column(name = "name", nullable = true) var name: String? = null
}
