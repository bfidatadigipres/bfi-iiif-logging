package uk.bfi.uvaudit.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken

class AuditUserStatelessAuthToken(
    jwt: Jwt,
    private val user: JwtAuditUser
) : AbstractOAuth2TokenAuthenticationToken<Jwt>(jwt) {

    override fun getPrincipal(): AuditUser = user
    override fun getTokenAttributes(): MutableMap<String, Any> = token.claims
}
