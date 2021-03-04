package uk.bfi.uvaudit.security

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException


class AuditUserService(
    private val delegate: OidcUserService = OidcUserService()
) : OidcUserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OidcUserRequest): AuditUser {
        val oidcUser = delegate.loadUser(userRequest)
        val id = 1L

        return AuditUser(id, oidcUser)
    }
}
