package uk.bfi.uvaudit.security

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import javax.sql.DataSource


class AuditUserService(
    private val ds: DataSource,
    private val delegate: OidcUserService = OidcUserService()
) : OidcUserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OidcUserRequest): AuditUser {
        val oidcUser = delegate.loadUser(userRequest)
        val template = NamedParameterJdbcTemplate(ds)

        val sqlParams = mapOf("sub" to oidcUser.subject)
        var dbUserId = template.queryForObject(
            "SELECT FROM user where sub = :sub",
            MapSqlParameterSource(sqlParams),
            Long::class.java
        )

        val userId = if (dbUserId == null) {
            val keyHolder = GeneratedKeyHolder()
            val sql = "INSERT INTO user (sub) VALUES (:sub)";

            template.update(sql, MapSqlParameterSource(sqlParams), keyHolder)
            keyHolder.key!!.toLong()
        } else {
            dbUserId
        }

        return AuditUser(userId, oidcUser)
    }
}
