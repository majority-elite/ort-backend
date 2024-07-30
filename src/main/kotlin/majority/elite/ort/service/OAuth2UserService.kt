package majority.elite.ort.service

import lombok.RequiredArgsConstructor
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class OAuth2UserService : DefaultOAuth2UserService() {
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User  {
        val user = super.loadUser(userRequest)
        val authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN")
        val userNameAttributeName = userRequest.clientRegistration
                .providerDetails
                .userInfoEndpoint
                .userNameAttributeName

        return DefaultOAuth2User(authorities, user.attributes, userNameAttributeName)
    }
}