package uk.bfi.uvaudit.security

import org.springframework.security.core.AuthenticatedPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
interface AuditUser {
    val id: Long
    val verified: Boolean
}

data class AuditUserDetails(val subject: String, val email: String, val department: String?)

class OidcAuditUser(
    override val id: Long,
    override val verified: Boolean,
    private val delegate: OidcUser
) : AuditUser, OidcUser by delegate

class JwtAuditUser(override val id: Long, override val verified: Boolean) : AuthenticatedPrincipal, AuditUser {
    override fun getName() = id.toString()
}
