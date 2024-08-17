package majority.elite.ort.service

import java.util.*
import lombok.RequiredArgsConstructor
import majority.elite.ort.domain.OAuthType
import majority.elite.ort.domain.UserDetailsImpl
import majority.elite.ort.entity.UserEntity
import majority.elite.ort.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class OAuth2UserService(private val userRepository: UserRepository) : DefaultOAuth2UserService() {
  @Throws(OAuth2AuthenticationException::class)
  override fun loadUser(userRequest: OAuth2UserRequest): UserDetailsImpl {
    val user = super.loadUser(userRequest)
    val userNameAttributeName =
      userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
    val serviceType = userRequest.clientRegistration.registrationId

    if (!serviceType.equals("kakao")) {
      println("Invalid OAuth2 service type given")
      throw OAuth2AuthenticationException("""Invalid OAuth2 service: $serviceType""")
    }

    println("signing in with OAuth2...")

    val userEntity = this.loginWithKakao(user.attributes[userNameAttributeName].toString())

    println("""user ${user.attributes[userNameAttributeName]} signed in with OAuth2.""")

    return UserDetailsImpl.fromUserEntity(userEntity)
  }

  fun loginWithKakao(oauthId: String): UserEntity {
    println(userRepository.findByOauthId(oauthId).toString())

    val existingUser = userRepository.findByOauthId(oauthId)

    if (existingUser == null) {
      println("Creating new user...")
      val userEntity = UserEntity(oauthId, OAuthType.KAKAO)
      userRepository.save(userEntity)
      return userEntity
    } else return existingUser
  }

  fun getUserIdWithOAuthId(oauthId: String): Long? {
    return userRepository.findByOauthId(oauthId)?.id
  }
}
