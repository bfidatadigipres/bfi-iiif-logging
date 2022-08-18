package uk.bfi.uvaudit.security.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import uk.bfi.uvaudit.security.AuditUserDetails
import uk.bfi.uvaudit.security.AuditUserStatelessAuthToken
import uk.bfi.uvaudit.security.JwtAuditUser
import uk.bfi.uvaudit.security.repository.AuditUserDetailsRepository

@Component
class AuditUserJwtAuthenticationConverter(val userDetails: AuditUserDetailsRepository) :
    Converter<Jwt, AuditUserStatelessAuthToken> {
    override fun convert(source: Jwt): AuditUserStatelessAuthToken? {
        val orgClaims = source.getClaimAsMap("https://bfi.org.uk/") ?: emptyMap()
        val details = AuditUserDetails(
            source.subject, source.getClaimAsString("email"),
            orgClaims["department"] as String?
        )

        val id = userDetails.lookupOrCreate(details)
        val user = JwtAuditUser(id)

        return AuditUserStatelessAuthToken(source, user)
    }

}
