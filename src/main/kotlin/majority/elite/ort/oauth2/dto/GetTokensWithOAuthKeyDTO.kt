package majority.elite.ort.oauth2.dto

class GetTokensWithOAuthKeyDTO(
  val userId: Long,
  val accessToken: String,
  val accessTokenExpiresAt: String,
  val refreshToken: String,
  val refreshTokenExpiresAt: String,
)
