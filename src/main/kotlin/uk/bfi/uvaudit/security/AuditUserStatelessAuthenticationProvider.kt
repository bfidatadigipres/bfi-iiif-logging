package uk.bfi.uvaudit.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class AuditUserStatelessAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        return authentication // if we wanted to check if the token had been revoked, we'd do it here
    }

    override fun supports(authentication: Class<*>): Boolean {
        return AuditUserStatelessAuthToken::class.java.isAssignableFrom(authentication)
    }
}
