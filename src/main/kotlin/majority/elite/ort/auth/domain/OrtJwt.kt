package majority.elite.ort.auth.domain

import java.text.SimpleDateFormat
import java.util.Date

class OrtJwt(val tokenValue: String, private val expiresAt: Date?) {
  fun getExpiresAtIsoString(): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(this.expiresAt)
  }
}
