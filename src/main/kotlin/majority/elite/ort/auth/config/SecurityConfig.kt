package majority.elite.ort.auth.config

import lombok.RequiredArgsConstructor
import majority.elite.ort.db.redis.RedisService
import majority.elite.ort.oauth2.config.OAuth2SuccessHandler
import majority.elite.ort.oauth2.config.OrtOAuth2AuthRequestResolver
import majority.elite.ort.oauth2.service.OAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig(
  private val oauth2UserService: OAuth2UserService,
  private val clientRegistrationRepo: ClientRegistrationRepository,
  private val redisService: RedisService,
) {
  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http {
      cors {
        configurationSource = CorsConfigurationSource { _ ->
          val configuration = CorsConfiguration()
          configuration.maxAge = 3600L
          configuration.allowedHeaders = listOf("*")
          configuration.exposedHeaders = listOf("Authorization")
          configuration.allowedOrigins = listOf("*")
          configuration.allowCredentials = true
          configuration
        }
      }
      authorizeHttpRequests { authorize(anyRequest, permitAll) }
      oauth2Login {
        userInfoEndpoint {
          // 로그인 시 OAuth2UserService의 loadUser 호출
          userInfoEndpoint {
            userService = oauth2UserService
            authorizationEndpoint {
              authorizationRequestResolver = OrtOAuth2AuthRequestResolver(clientRegistrationRepo)
            }
          }
          authenticationSuccessHandler = OAuth2SuccessHandler(redisService)
        }
      }
      formLogin { disable() }
      httpBasic { disable() }
      csrf { disable() }
    }

    return http.build()
  }
}
