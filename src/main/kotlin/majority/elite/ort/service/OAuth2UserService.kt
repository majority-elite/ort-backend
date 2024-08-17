package majority.elite.ort.service

import java.util.*
import lombok.RequiredArgsConstructor
import majority.elite.ort.domain.OAuthType
import majority.elite.ort.domain.UserDetailsImpl
import majority.elite.ort.exception.KakaoApiFailureException
import majority.elite.ort.repository.UserRepository
import org.springframework.http.*
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class OAuth2UserService(
  private val userRepository: UserRepository,
  private val kakaoService: KakaoService,
) : DefaultOAuth2UserService() {
  @Throws(OAuth2AuthenticationException::class)
  override fun loadUser(userRequest: OAuth2UserRequest): UserDetailsImpl {
    val user = super.loadUser(userRequest)
    val userNameAttributeName =
      userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
    val serviceType = userRequest.clientRegistration.registrationId

    if (!serviceType.equals("kakao")) {
      throw OAuth2AuthenticationException("""Invalid OAuth2 service: $serviceType""")
    }

    val userEntity = kakaoService.loginWithKakao(user.attributes[userNameAttributeName].toString())

    return UserDetailsImpl.fromUserEntity(userEntity)
  }

  @Throws(KakaoApiFailureException::class)
  fun logout(userId: Long) {
    val userEntity = userRepository.findById(userId)

    when (userEntity.get().oauthType) {
      OAuthType.KAKAO -> {
        kakaoService.logoutKakao(userEntity.get().oauthId!!.toLong())
      }

      else -> {
        throw IllegalArgumentException()
      }
    }
  }

  @Throws(KakaoApiFailureException::class)
  fun deleteUser(userId: Long) {
    val userEntity = userRepository.findById(userId)

    when (userEntity.get().oauthType) {
      OAuthType.KAKAO -> {
        kakaoService.unlinkKakao(userEntity.get().oauthId!!.toLong())
      }

      else -> {
        throw IllegalArgumentException()
      }
    }

    userRepository.delete(userEntity.get())
  }
}
