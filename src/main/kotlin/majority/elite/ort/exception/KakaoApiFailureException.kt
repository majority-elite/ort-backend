package majority.elite.ort.exception

class KakaoApiFailureException(private val errorResponse: String) : Exception() {
  val statusCode = 500

  override val message: String
    get() = "카카오 API 통신에 문제가 발생했습니다: $errorResponse"
}
