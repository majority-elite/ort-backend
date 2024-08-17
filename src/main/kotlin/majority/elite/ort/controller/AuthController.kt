import lombok.RequiredArgsConstructor
import majority.elite.ort.dto.RefreshAccessTokenRequestDTO
import majority.elite.ort.service.OAuth2UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth")
class OAuth2Controller {
  @PostMapping("/access")
  fun refreshAccessToken(@RequestBody body: RefreshAccessTokenRequestDTO) {

  }
}
