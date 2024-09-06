package majority.elite.ort.oauth2.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

// src/main/resources/api.properties에 값 작성
@Configuration
@ConfigurationProperties(prefix = "oauth2.kakao")
class KakaoConfig {
  var logoutUri: String = ""
  var adminKey: String = ""
  var unlinkUri: String = ""
  var authorizationUri: String = ""
}
