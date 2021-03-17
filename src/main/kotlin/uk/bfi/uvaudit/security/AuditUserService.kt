package uk.bfi.uvaudit.security

import org.springframework.dao.support.DataAccessUtils
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import javax.sql.DataSource

class AuditUserService(
    dataSource: DataSource,
    private val delegate: OidcUserService = OidcUserService()
) : OidcUserService() {

    private val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OidcUserRequest): AuditUser {
        val oidcUser = delegate.loadUser(userRequest)
        val auditUser = fetchUser(oidcUser)
        return if (auditUser == null) {
            createUser(oidcUser)
        } else {
            updateUser(oidcUser, auditUser)
        }
    }

    private fun fetchUser(oidcUser: OidcUser): AuditUser? {
        val sql = "SELECT id FROM user where sub = :sub"
        val params = MapSqlParameterSource(
            mapOf("sub" to oidcUser.subject)
        )

        val dbUserIds = jdbcTemplate.queryForList(sql, params, Long::class.java)
        val userId = DataAccessUtils.uniqueResult(dbUserIds)

        return if (userId != null) {
            AuditUser(userId, oidcUser)
        } else {
            null
        }
    }

    private fun createUser(oidcUser: OidcUser): AuditUser {
        val sql = "INSERT INTO user (sub, email, department) VALUES (:sub, :email, :department)"
        val params = MapSqlParameterSource(
            mapOf(
                "sub" to oidcUser.subject,
                "email" to oidcUser.email,
                "department" to extractDepartmentClaim(oidcUser)
            )
        )
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(sql, params, keyHolder)

        return AuditUser(keyHolder.key!!.toLong(), oidcUser)
    }

    private fun updateUser(oidcUser: OidcUser, auditUser: AuditUser): AuditUser {
        val sql = "UPDATE user SET email = :email, department = :department WHERE id = :id"
        val params = MapSqlParameterSource(
            mapOf(
                "email" to oidcUser.email,
                "department" to extractDepartmentClaim(oidcUser),
                "id" to auditUser.id
            )
        )

        jdbcTemplate.update(sql, params)

        return AuditUser(auditUser.id, oidcUser)
    }

    private fun extractDepartmentClaim(oidcUser: OidcUser): String? {
        val customClaims = oidcUser.getClaimAsMap("https://bfi.org.uk/")
        return if (customClaims == null) {
            null
        } else {
            customClaims["department"] as String?
        }
    }
}
