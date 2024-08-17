package majority.elite.ort.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
  var secret: String = ""
  var accessTokenExpiresIn: Long = 0L
  var refreshTokenExpiresIn: Long = 0L
  var issuer: String = ""
}
