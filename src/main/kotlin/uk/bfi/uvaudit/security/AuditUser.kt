package uk.bfi.uvaudit.security

import org.springframework.security.oauth2.core.oidc.user.OidcUser

class AuditUser(
    val id: Long,
    private val delegate: OidcUser
) : OidcUser by delegate
