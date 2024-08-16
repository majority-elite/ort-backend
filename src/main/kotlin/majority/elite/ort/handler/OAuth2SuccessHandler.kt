package majority.elite.ort.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import majority.elite.ort.component.OAuth2ClientProvider
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@RequiredArgsConstructor
class OAuth2SuccessHandler(private val oAuth2ClientProvider: OAuth2ClientProvider) :
  AuthenticationSuccessHandler {
  override fun onAuthenticationSuccess(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    authentication: Authentication?,
  ) {
    val client = oAuth2ClientProvider.getClient()
    val writer = response!!.writer

    writer.write(
      """
      {
        "accessToken": "${client.accessToken.tokenValue}"
        "accessTokenExpiresAt": "${client.accessToken!!.expiresAt}"
        "refreshToken": "${client.refreshToken?.tokenValue}"
        "refreshTokenExpiresAt": "${client.refreshToken?.expiresAt}"
      }
      """
        .trimIndent()
    )

    writer.flush()
  }
}
