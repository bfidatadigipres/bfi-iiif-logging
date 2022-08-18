package uk.bfi.uvaudit.security

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component
import uk.bfi.uvaudit.security.repository.AuditUserDetailsRepository

@Component
class AuditUserService(
    private val userDetails: AuditUserDetailsRepository,
    private val delegate: OidcUserService = OidcUserService()
) : OidcUserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OidcUserRequest): OidcAuditUser {
        val delegate = delegate.loadUser(userRequest)
        val userId = userDetails.lookupOrCreate(
            AuditUserDetails(
                delegate.subject, delegate.email, extractDepartmentClaim(delegate)
            )
        )

        return OidcAuditUser(userId, delegate)
    }

    private fun extractDepartmentClaim(oidcUser: OidcUser): String? {
        val customClaims = oidcUser.getClaimAsMap("https://bfi.org.uk/")
        return customClaims?.get("department") as String?
    }
}
