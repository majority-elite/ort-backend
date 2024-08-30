package majority.elite.ort.controller

import majority.elite.ort.service.OAuth2UserService
import majority.elite.ort.service.OrtJwtService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
  private val oAuth2UserService: OAuth2UserService,
  private val ortJwtService: OrtJwtService,
) {
  @DeleteMapping
  fun deleteUser(@RequestHeader("Authorization") bearerToken: String): String {
    val accessToken = bearerToken.split(" ")[1]
    val userId = (ortJwtService.verifyToken(accessToken)["userId"] as String).toLong()

    oAuth2UserService.deleteUser(userId)

    return "OK"
  }
}
