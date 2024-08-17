package majority.elite.ort.domain

import java.text.SimpleDateFormat
import java.util.Date

class OrtJwt(val tokenValue: String, val expiresAt: Date?) {
  fun getExpiresAtIsoString(): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'").format(this.expiresAt)
  }
}
