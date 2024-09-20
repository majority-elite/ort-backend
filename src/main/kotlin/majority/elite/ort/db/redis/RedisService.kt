package majority.elite.ort.db.redis

import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.stereotype.Service

@Service
class RedisService(private val redisConfig: RedisConfig) {
  private fun getConnectionFactory(): LettuceConnectionFactory {
    val connectionFactory =
      LettuceConnectionFactory(RedisStandaloneConfiguration(redisConfig.host, redisConfig.port))
    connectionFactory.standaloneConfiguration.password = RedisPassword.of(redisConfig.password)
    connectionFactory.afterPropertiesSet()

    return connectionFactory
  }

  fun setKeyValueBlocking(key: String, value: String) {
    val connectionFactory = getConnectionFactory()
    val template = ReactiveRedisTemplate(connectionFactory, RedisSerializationContext.string())

    template.opsForValue().set(key, value).block()
    connectionFactory.destroy()
  }

  fun getValueByKeyBlocking(key: String): Any? {
    val connectionFactory = getConnectionFactory()
    val template = ReactiveRedisTemplate(connectionFactory, RedisSerializationContext.string())

    val value = template.opsForValue().get(key).block()
    connectionFactory.destroy()

    return value
  }
}
