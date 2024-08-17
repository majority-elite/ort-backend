package majority.elite.ort.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import majority.elite.ort.dto.OAuth2SuccessResponseDTO
import majority.elite.ort.service.OrtJwtService
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@RequiredArgsConstructor
class OAuth2SuccessHandler(private val ortJwtService: OrtJwtService) : AuthenticationSuccessHandler {
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
