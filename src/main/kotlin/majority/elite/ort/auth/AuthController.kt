package majority.elite.ort.auth

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lombok.RequiredArgsConstructor
import majority.elite.ort.auth.dto.RefreshAccessTokenRequestDTO
import majority.elite.ort.auth.dto.RefreshAccessTokenResponseDTO
import majority.elite.ort.exception.UnauthorizedException
import majority.elite.ort.auth.exception.TokenExpiredException
import majority.elite.ort.oauth2.service.OAuth2UserService
import majority.elite.ort.auth.service.OrtJwtService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
class AuthController(
  private val ortJwtService: OrtJwtService,
  private val oAuth2UserService: OAuth2UserService,
) {
  @PostMapping("/token/access")
  @Throws(UnauthorizedException::class, TokenExpiredException::class)
  @ApiResponses(
    value =
      [
        ApiResponse(
          responseCode = "200",
          content =
            [
              Content(
                mediaType = "application/json",
                schema = Schema(implementation = RefreshAccessTokenResponseDTO::class),
              )
            ],
        )
      ]
  )
  fun refreshAccessToken(
    @RequestBody requestBody: RefreshAccessTokenRequestDTO
  ): RefreshAccessTokenResponseDTO {
    val userId = (ortJwtService.verifyToken(requestBody.refreshToken)["userId"] as String).toLong()
    val accessToken = ortJwtService.createAccessToken(userId)

    return RefreshAccessTokenResponseDTO(
      accessToken.tokenValue,
      accessToken.getExpiresAtIsoString(),
    )
  }

  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/logout")
  fun signOut(@RequestHeader("Authorization") bearerToken: String): String {
    val accessToken = bearerToken.split(" ")[1]
    val userId = (ortJwtService.verifyToken(accessToken)["userId"] as String).toLong()

    oAuth2UserService.logout(userId)

    return "OK"
  }
}
