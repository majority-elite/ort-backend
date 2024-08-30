package majority.elite.ort.exception

class UnauthorizedException : Exception() {
  val statusCode = 401
  override val message: String
    get() = "Unauthorized"
}
