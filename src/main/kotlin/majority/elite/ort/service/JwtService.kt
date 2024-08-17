package majority.elite.ort.service

import io.jsonwebtoken.Jwts
import java.util.Date
import majority.elite.ort.config.JwtConfig
import majority.elite.ort.domain.OrtJwt
import majority.elite.ort.exception.UnauthorizedException
import majority.elite.ort.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class JwtService(private val userRepository: UserRepository, private val jwtConfig: JwtConfig) {
  @Throws(UnauthorizedException::class)
  fun createAccessToken(userId: Long): OrtJwt {
    val user = userRepository.findById(userId)

    if (user.isEmpty) {
      throw UnauthorizedException()
    }

    val expiresAt = Date(Date().time + jwtConfig.accessTokenExpiresIn)

    return OrtJwt(
      Jwts.builder()
        .issuer("ort")
        .subject("""USER$userId""")
        .issuedAt(Date())
        .claim("userId", """$userId""")
        .claim("role", user.get().role.name)
        .expiration(expiresAt)
        .compact(),
      expiresAt,
    )
  }

  @Throws(UnauthorizedException::class)
  fun createRefreshToken(userId: Long): OrtJwt {
    val expiresAt = Date(Date().time + jwtConfig.accessTokenExpiresIn)

    return OrtJwt(
      Jwts.builder()
        .issuer("ort")
        .subject("""USER$userId""")
        .issuedAt(Date())
        .claim("userId", """$userId""")
        .expiration(Date(Date().time + jwtConfig.refreshTokenExpiresIn))
        .compact(),
      expiresAt,
    )
  }
}
