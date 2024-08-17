package majority.elite.ort.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "oauth2.kakao")
class KakaoConfig {
  var logoutUri: String = ""
  var adminKey: String = ""
}
