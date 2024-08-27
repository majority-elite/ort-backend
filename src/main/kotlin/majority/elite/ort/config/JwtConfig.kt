package majority.elite.ort.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

// src/main/resources/api.properties에 값 작성
@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
  var secret: String = ""
  // TODO("변수 이름에 단위(Seconds) 명시 필요")
  var accessTokenExpiresIn: Long = 0L
  var refreshTokenExpiresIn: Long = 0L
  var issuer: String = ""
}
