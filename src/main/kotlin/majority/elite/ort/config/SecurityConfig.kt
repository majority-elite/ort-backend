package majority.elite.ort.config

import lombok.RequiredArgsConstructor
import majority.elite.ort.handler.OAuth2SuccessHandler
import majority.elite.ort.service.OAuth2UserService
import majority.elite.ort.service.OrtJwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig(
  private val oauth2UserService: OAuth2UserService,
  private val ortJwtService: OrtJwtService,
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
          configuration
        }
      }
      authorizeHttpRequests { authorize(anyRequest, permitAll) }
      oauth2Login {
        userInfoEndpoint {
          userInfoEndpoint { userService = oauth2UserService }
          authenticationSuccessHandler = OAuth2SuccessHandler(ortJwtService)
        }
      }
      formLogin { disable() }
      httpBasic { disable() }
      csrf { disable() }
    }

    return http.build()
  }
}
