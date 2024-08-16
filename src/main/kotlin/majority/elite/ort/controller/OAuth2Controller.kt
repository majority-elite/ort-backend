import lombok.RequiredArgsConstructor
import majority.elite.ort.service.OAuth2UserService
import org.springframework.stereotype.Controller

@Controller
@RequiredArgsConstructor
class OAuth2Controller(private val oAuth2UserService: OAuth2UserService) {}
