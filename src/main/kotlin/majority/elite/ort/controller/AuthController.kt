package majority.elite.ort.controller

import javax.validation.Valid
import lombok.RequiredArgsConstructor
import majority.elite.ort.dto.RefreshAccessTokenRequestDTO
import majority.elite.ort.dto.RefreshAccessTokenResponseDTO
import majority.elite.ort.exception.TokenExpiredException
import majority.elite.ort.exception.UnauthorizedException
import majority.elite.ort.service.OAuth2UserService
import majority.elite.ort.service.OrtJwtService
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
  fun refreshAccessToken(
    @Valid @RequestBody requestBody: RefreshAccessTokenRequestDTO
  ): RefreshAccessTokenResponseDTO {
    val userId = (ortJwtService.verifyToken(requestBody.refreshToken)["userId"] as String).toLong()
    val accessToken = ortJwtService.createAccessToken(userId)

    return RefreshAccessTokenResponseDTO(
      accessToken.tokenValue,
      accessToken.getExpiresAtIsoString(),
    )
  }

  @GetMapping("/logout")
  fun signOut(@RequestHeader("Authorization") bearerToken: String): String {
    val accessToken = bearerToken.split(" ")[1]
    val userId = (ortJwtService.verifyToken(accessToken)["userId"] as String).toLong()

    oAuth2UserService.logout(userId)

    return "OK"
  }
}
