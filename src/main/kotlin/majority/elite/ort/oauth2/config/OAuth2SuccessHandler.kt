package majority.elite.ort.oauth2.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import majority.elite.ort.db.redis.RedisService
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@RequiredArgsConstructor
class OAuth2SuccessHandler(private val redisService: RedisService) : AuthenticationSuccessHandler {
  /*
  회원가입 혹은 Refresh Token 만료 시 OAuth2 재로그인 필요
  새로운 Refresh Token과 Access Token 발급하여 제공
  */
  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication,
  ) {
    // "{UUID}|{redirectTo}"
    val tokenizedState = request.getParameter("state").toString().split("|")

    val oauthKey = tokenizedState[0]
    val redirectTo = tokenizedState[1]

    val userId = authentication.name.toLong()

    redisService.setKeyValueBlocking(oauthKey, "$userId")

    response.sendRedirect("${redirectTo}?oauthKey=${oauthKey}")
  }
}
