package majority.elite.ort.exception.auth

class TokenExpiredException : Exception() {
  val statusCode = 401

  override val message: String
    get() = "Token Expired"
}
