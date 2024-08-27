package majority.elite.ort.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import majority.elite.ort.config.JwtConfig
import majority.elite.ort.domain.OrtJwt
import majority.elite.ort.exception.UnauthorizedException
import majority.elite.ort.exception.auth.TokenExpiredException
import majority.elite.ort.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class OrtJwtService(private val userRepository: UserRepository, private val jwtConfig: JwtConfig) {
  private fun getSignInKey(): SecretKeySpec {
    val bytes = Base64.getEncoder().encode(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8))
    return SecretKeySpec(bytes, "HmacSHA256")
  }

  @Throws(UnauthorizedException::class)
  fun createAccessToken(userId: Long): OrtJwt {
    val user = userRepository.findById(userId)

    if (user.isEmpty) {
      throw UnauthorizedException()
    }

    val expiresAt = Date(Date().time + jwtConfig.accessTokenExpiresIn)

    return OrtJwt(
      Jwts.builder()
        .issuer(jwtConfig.issuer)
        .subject("USER$userId")
        .issuedAt(Date())
        .claim("userId", "$userId")
        .claim("role", user.get().role.name)
        .expiration(expiresAt)
        .signWith(getSignInKey())
        .compact(),
      expiresAt,
    )
  }

  @Throws(UnauthorizedException::class)
  fun createRefreshToken(userId: Long): OrtJwt {
    val expiresAt = Date(Date().time + jwtConfig.refreshTokenExpiresIn)

    return OrtJwt(
      Jwts.builder()
        .issuer(jwtConfig.issuer)
        .subject("USER$userId")
        .issuedAt(Date())
        .claim("userId", "$userId")
        .expiration(expiresAt)
        .signWith(getSignInKey())
        .compact(),
      expiresAt,
    )
  }

  private fun parseClaims(token: String): Claims {
    return Jwts.parser().verifyWith(this.getSignInKey()).build().parseSignedClaims(token).payload
  }

  // 토큰 유효성 검사 및 Claim 추출하여 반환
  @Throws(TokenExpiredException::class, IllegalArgumentException::class, JwtException::class)
  fun verifyToken(token: String): Claims {
    val parsedClaims = this.parseClaims(token)

    if (parsedClaims.expiration.before(Date())) {
      throw TokenExpiredException()
    }

    return parsedClaims
  }
}
