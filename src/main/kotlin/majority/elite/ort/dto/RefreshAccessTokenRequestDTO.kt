package majority.elite.ort.dto

import javax.validation.constraints.NotBlank
import lombok.NoArgsConstructor

@NoArgsConstructor
class RefreshAccessTokenRequestDTO() {
  @NotBlank(message = "No refresh token provided") val refreshToken: String = ""
}
