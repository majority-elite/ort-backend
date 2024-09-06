package majority.elite.ort.oauth2.service

import majority.elite.ort.oauth2.config.KakaoConfig
import majority.elite.ort.oauth2.domain.OAuthType
import majority.elite.ort.user.UserEntity
import majority.elite.ort.oauth2.exception.KakaoApiFailureException
import majority.elite.ort.user.UserRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class KakaoService(
  private val kakaoConfig: KakaoConfig,
  private val userRepository: UserRepository,
) {
  fun loginWithKakao(oauthId: String): UserEntity {
    val existingUser = userRepository.findByOauthId(oauthId)

    // 첫 로그인 시 DB에 신규 사용자 추가
    if (existingUser == null) {
      val userEntity = UserEntity(oauthId, OAuthType.KAKAO)
      userRepository.save(userEntity)
      return userEntity
    } else return existingUser
  }

  // 참고: https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#logout
  @Throws(KakaoApiFailureException::class)
  fun logoutKakao(oauthId: Long) {
    val restTemplate = RestTemplate()
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
    headers.set("Authorization", "KakaoAK ${kakaoConfig.adminKey}")

    // 요청 Body
    val map = LinkedMultiValueMap<String, String>()
    map.add("target_id_type", "user_id")
    map.add("target_id", oauthId.toString())

    val httpEntity = HttpEntity<MultiValueMap<String, String>>(map, headers)

    try {
      restTemplate.exchange(kakaoConfig.logoutUri, HttpMethod.POST, httpEntity, String::class.java)
    } catch (badRequestException: HttpClientErrorException.BadRequest) {
      throw KakaoApiFailureException(badRequestException.message!!)
    }
  }

  // 참고: https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#unlink
  @Throws(KakaoApiFailureException::class)
  fun unlinkKakao(oauthId: Long) {
    val restTemplate = RestTemplate()
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
    headers.set("Authorization", "KakaoAK ${kakaoConfig.adminKey}")

    // 요청 Body
    val map = LinkedMultiValueMap<String, String>()
    map.add("target_id_type", "user_id")
    map.add("target_id", oauthId.toString())

    val httpEntity = HttpEntity<MultiValueMap<String, String>>(map, headers)

    try {
      restTemplate.exchange(kakaoConfig.unlinkUri, HttpMethod.POST, httpEntity, String::class.java)
    } catch (badRequestException: HttpClientErrorException.BadRequest) {
      throw KakaoApiFailureException(badRequestException.message!!)
    }
  }
}
