package majority.elite.ort.oauth2.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import majority.elite.ort.oauth2.dto.OAuth2SuccessResponseDTO
import majority.elite.ort.auth.service.OrtJwtService
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@RequiredArgsConstructor
class OAuth2SuccessHandler(private val ortJwtService: OrtJwtService) :
  AuthenticationSuccessHandler {
  /*
  회원가입 혹은 Refresh Token 만료 시 OAuth2 재로그인 필요
  새로운 Refresh Token과 Access Token 발급하여 제공
  */
  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication,
  ) {
    val writer = response.writer
    val userId = authentication.name.toLong()

    val accessToken = ortJwtService.createAccessToken(userId)
    val refreshToken = ortJwtService.createRefreshToken(userId)

    writer.write(OAuth2SuccessResponseDTO(userId, accessToken, refreshToken).toString())
    writer.flush()
  }
}
