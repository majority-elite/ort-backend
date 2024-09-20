package majority.elite.ort.db.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "db.redis")
class RedisConfig {
  var host = ""
  var port = 0
  var database = 0
  var password = ""
}
