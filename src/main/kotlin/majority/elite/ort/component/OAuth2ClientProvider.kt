package majority.elite.ort.component

import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component

// TODO("사용하지 않는 코드, 삭제 필요")
@Component
@RequiredArgsConstructor
class OAuth2ClientProvider(private val clientService: OAuth2AuthorizedClientService) {
  fun getClient(): OAuth2AuthorizedClient {
    val authentication = SecurityContextHolder.getContext().authentication
    val oauthToken = authentication as OAuth2AuthenticationToken
    return clientService.loadAuthorizedClient(
      oauthToken.authorizedClientRegistrationId,
      oauthToken.name,
    )
  }
}
