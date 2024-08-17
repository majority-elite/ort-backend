package majority.elite.ort.dto

import java.text.SimpleDateFormat
import majority.elite.ort.domain.OrtJwt

class OAuth2SuccessResponseDTO(
  private val userId: Long,
  private val accessToken: OrtJwt,
  private val refreshToken: OrtJwt,
) {
  override fun toString(): String {
    return """
      {
        "userId": $userId
        "accessToken": "${accessToken.tokenValue}"
        "accessTokenExpiresAt": "${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'").format(accessToken.expiresAt)}"
        "refreshToken": "${refreshToken.tokenValue}"
        "refreshTokenExpiresAt": "${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'").format(refreshToken.expiresAt)}"
      }
      """
      .trimIndent()
  }
}
