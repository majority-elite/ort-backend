package majority.elite.ort.dto

import majority.elite.ort.domain.OrtJwt

// TODO("add validation")
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
        "accessTokenExpiresAt": "${accessToken.getExpiresAtIsoString()}"
        "refreshToken": "${refreshToken.tokenValue}"
        "refreshTokenExpiresAt": "${refreshToken.getExpiresAtIsoString()}"
      }
      """
      .trimIndent()
  }
}
