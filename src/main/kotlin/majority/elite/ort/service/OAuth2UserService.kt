package majority.elite.ort.service

import java.util.*
import lombok.RequiredArgsConstructor
import majority.elite.ort.config.KakaoConfig
import majority.elite.ort.domain.OAuthType
import majority.elite.ort.domain.UserDetailsImpl
import majority.elite.ort.entity.UserEntity
import majority.elite.ort.repository.UserRepository
import org.springframework.http.*
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException.BadRequest
import org.springframework.web.client.RestTemplate

@Service
@RequiredArgsConstructor
class OAuth2UserService(
  private val userRepository: UserRepository,
  private val kakaoConfig: KakaoConfig,
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

    val userEntity = this.loginWithKakao(user.attributes[userNameAttributeName].toString())

    return UserDetailsImpl.fromUserEntity(userEntity)
  }

  fun loginWithKakao(oauthId: String): UserEntity {
    val existingUser = userRepository.findByOauthId(oauthId)

    if (existingUser == null) {
      val userEntity = UserEntity(oauthId, OAuthType.KAKAO)
      userRepository.save(userEntity)
      return userEntity
    } else return existingUser
  }

  private fun logoutKakao(oauthId: Long) {
    println("kakao user $oauthId tries logout with admin key ${kakaoConfig.adminKey}")

    val restTemplate = RestTemplate()
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
    headers.set("Authorization", "KakaoAK ${kakaoConfig.adminKey}")

    val map = LinkedMultiValueMap<String, String>()
    map.add("target_id_type", "user_id")
    map.add("target_id", oauthId.toString())

    val httpEntity = HttpEntity<MultiValueMap<String, String>>(map, headers)

    try {
      restTemplate.exchange(kakaoConfig.logoutUri, HttpMethod.POST, httpEntity, String::class.java)
    } catch (badRequestException: BadRequest) {
      throw Exception("카카오 로그인에 실패했습니다. ${badRequestException.message}")
    }

    println("Logout succeed")
  }

  @Throws(IllegalArgumentException::class, Exception::class)
  fun logout(userId: Long) {
    val userEntity = userRepository.findById(userId)

    println(userEntity.get().oauthId)

    when (userEntity.get().oauthType) {
      OAuthType.KAKAO -> {
        logoutKakao(userEntity.get().oauthId!!.toLong())
      }

      else -> {
        throw IllegalArgumentException()
      }
    }
  }
}
