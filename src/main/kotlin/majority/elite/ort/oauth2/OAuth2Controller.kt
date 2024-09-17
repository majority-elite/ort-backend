package majority.elite.ort.oauth2

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import lombok.RequiredArgsConstructor
import majority.elite.ort.auth.service.OrtJwtService
import majority.elite.ort.db.redis.RedisService
import majority.elite.ort.exception.UnauthorizedException
import majority.elite.ort.oauth2.dto.GetTokensWithOAuthKeyDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2")
class OAuth2Controller(
  private val redisService: RedisService,
  private val ortJwtService: OrtJwtService,
) {
  @GetMapping("/token")
  @ApiResponses(
    value =
      [
        ApiResponse(
          responseCode = "200",
          content =
            [
              Content(
                mediaType = "application/json",
                schema = Schema(implementation = GetTokensWithOAuthKeyDTO::class),
              )
            ],
        )
      ]
  )
  fun getTokensWithOauthKey(@RequestParam oauthKey: String): GetTokensWithOAuthKeyDTO {
    val userId = redisService.getValueByKeyBlocking(oauthKey) ?: throw UnauthorizedException()

    val userIdLong = userId.toString().toLong()

    val accessToken = ortJwtService.createAccessToken(userIdLong)
    val refreshToken = ortJwtService.createRefreshToken(userIdLong)

    return GetTokensWithOAuthKeyDTO(
      userIdLong,
      accessToken.tokenValue,
      accessToken.getExpiresAtIsoString(),
      refreshToken.tokenValue,
      refreshToken.getExpiresAtIsoString(),
    )
  }
}
