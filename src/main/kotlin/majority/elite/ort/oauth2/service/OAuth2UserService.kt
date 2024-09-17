package majority.elite.ort.oauth2.service

import java.util.*
import lombok.RequiredArgsConstructor
import majority.elite.ort.auth.domain.AuthDetails
import majority.elite.ort.oauth2.domain.OAuthType
import majority.elite.ort.oauth2.exception.KakaoApiFailureException
import majority.elite.ort.user.UserRepository
import org.springframework.http.*
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.stereotype.Service

/*
 * 현재 OAuth2 외 로그인 방법 추가 예정이 없어 본 코드에 로그인, 로그아웃 등 유저 DB 조작 관련 코드 포함
 * OAuth2 외 로그인 방법 추가 시 UserService 생성하여 해당 서비스에서 사용자 DB 관리, 본 서비스에서는 OAuth2 관련 코드만 포함
 */
@Service
@RequiredArgsConstructor
class OAuth2UserService(
  private val userRepository: UserRepository,
  private val kakaoService: KakaoService,
) : DefaultOAuth2UserService() {
  // OAuth2 로그인 시 호출됨
  @Throws(OAuth2AuthenticationException::class)
  override fun loadUser(userRequest: OAuth2UserRequest): AuthDetails {
    val user = super.loadUser(userRequest)

    // 사용자의 고유 id 값을 저장할 attribute명 (카카오 로그인의 경우 "id")
    val userNameAttributeName =
      userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

    // application.yaml에 명시된 서비스 provider명 (카카오 로그인의 경우 "kakao") (
    val serviceType = userRequest.clientRegistration.registrationId

    // 타 OAuth2 로그인 기능 추가시 when 문으로 변경 필요, 현재는 kakao 로그인만 취급
    if (!serviceType.equals("kakao")) {
      throw OAuth2AuthenticationException("""Invalid OAuth2 service: $serviceType""")
    }

    val userEntity = kakaoService.loginWithKakao(user.attributes[userNameAttributeName].toString())

    return AuthDetails.fromUserEntity(userEntity)
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

  fun registerOAuthKey(userId: Long) {

  }
}
