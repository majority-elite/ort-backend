package majority.elite.ort.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

// src/main/resources/api.properties에 값 작성
@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
  var secret: String = ""
  var accessTokenExpiresInSeconds: Long = 0L
  var refreshTokenExpiresInSeconds: Long = 0L
  var issuer: String = ""
}
