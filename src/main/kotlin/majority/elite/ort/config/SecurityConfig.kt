package majority.elite.ort.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.nio.charset.StandardCharsets
import lombok.RequiredArgsConstructor
import majority.elite.ort.service.OAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig(private val oauth2UserService: OAuth2UserService) {
  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http {
      authorizeHttpRequests { authorize(anyRequest, permitAll) }
      oauth2Login {
        userInfoEndpoint {
          userInfoEndpoint { userService = oauth2UserService }
          authenticationSuccessHandler = successHandler()
        }
      }
      sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
      formLogin { disable() }
      httpBasic { disable() }
      csrf { disable() }
    }

    return http.build()
  }

  @Bean
  fun successHandler(): AuthenticationSuccessHandler {
    return (AuthenticationSuccessHandler {
      request: HttpServletRequest?,
      response: HttpServletResponse,
      authentication: Authentication ->
      val defaultOAuth2User = authentication.principal as DefaultOAuth2User
      val id = defaultOAuth2User.attributes["id"].toString()
      val body =
        """
          {
            "id": "$id"
          }
        """
          .trimIndent()

      response.contentType = MediaType.APPLICATION_JSON_VALUE
      response.characterEncoding = StandardCharsets.UTF_8.name()

      val writer = response.writer
      writer.println(body)
      writer.flush()
    })
  }
}
