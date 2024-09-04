package majority.elite.ort.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import majority.elite.ort.exception.UnauthorizedException
import majority.elite.ort.exception.auth.TokenExpiredException
import majority.elite.ort.service.OAuth2UserService
import majority.elite.ort.service.OrtJwtService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
  private val oAuth2UserService: OAuth2UserService,
  private val ortJwtService: OrtJwtService,
) {
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping
  @Throws(UnauthorizedException::class, TokenExpiredException::class)
  fun deleteUser(@RequestHeader("Authorization") bearerToken: String): String {
    val accessToken = bearerToken.split(" ")[1]
    val userId = (ortJwtService.verifyToken(accessToken)["userId"] as String).toLong()

    oAuth2UserService.deleteUser(userId)

    return "OK"
  }
}
