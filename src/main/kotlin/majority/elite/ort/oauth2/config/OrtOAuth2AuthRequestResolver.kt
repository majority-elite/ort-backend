package majority.elite.ort.oauth2.config

import jakarta.servlet.http.HttpServletRequest
import java.util.UUID
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest

class OrtOAuth2AuthRequestResolver(clientRegistrationRepository: ClientRegistrationRepository) :
  OAuth2AuthorizationRequestResolver {
  private val defaultAuthorizationRequestResolver =
    DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization")

  override fun resolve(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
    val oauth2Request = defaultAuthorizationRequestResolver.resolve(request) ?: return null

    return createOrtAuthorizationRequest(oauth2Request, request)
  }

  override fun resolve(
    request: HttpServletRequest?,
    clientRegistrationId: String,
  ): OAuth2AuthorizationRequest? {
    val oauth2Request =
      defaultAuthorizationRequestResolver.resolve(request, clientRegistrationId) ?: return null
    return createOrtAuthorizationRequest(oauth2Request, request)
  }

  private fun createOrtAuthorizationRequest(
    oauth2Request: OAuth2AuthorizationRequest,
    httpRequest: HttpServletRequest?,
  ): OAuth2AuthorizationRequest {
    val additionalParameters = LinkedHashMap<String, Any>(oauth2Request.additionalParameters)
    additionalParameters["redirect_to"] =
      httpRequest?.getParameter("redirect_to") ?: { additionalParameters["redirect_to"] = "none" }

    return OAuth2AuthorizationRequest.from(oauth2Request)
      .state("${UUID.randomUUID()}|${httpRequest?.getParameter("redirect_to")}")
      .build()
  }
}
