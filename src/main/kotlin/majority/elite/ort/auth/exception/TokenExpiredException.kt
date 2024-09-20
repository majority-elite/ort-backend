package majority.elite.ort.auth.exception

class TokenExpiredException : Exception() {
  val statusCode = 401

  override val message: String
    get() = "Token Expired"
}
