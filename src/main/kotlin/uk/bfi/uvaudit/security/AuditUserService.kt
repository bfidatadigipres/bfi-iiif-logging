package uk.bfi.uvaudit.security

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.dao.IncorrectResultSizeDataAccessException
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
        with(fetchUser(oidcUser)) {
            return this ?: createUser(oidcUser)
        }
    }

    private fun fetchUser(oidcUser: OidcUser): AuditUser? {
        val sql = "SELECT id FROM user where sub = :sub"
        val params = MapSqlParameterSource(
            mapOf("sub" to oidcUser.subject)
        )

        val dbUserIds = jdbcTemplate.queryForList(sql, params, Long::class.java)

        return when {
            dbUserIds.isEmpty() -> {
                null
            }
            dbUserIds.size == 1 -> {
                AuditUser(dbUserIds[0], oidcUser)
            }
            else -> {
                throw IncorrectResultSizeDataAccessException(0, dbUserIds.size)
            }
        }
    }

    private fun createUser(oidcUser: OidcUser): AuditUser {
        val sql = "INSERT INTO user (sub) VALUES (:sub)"
        val params = MapSqlParameterSource(
            mapOf("sub" to oidcUser.subject)
        )
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(sql, params, keyHolder)

        return when (keyHolder.key) {
            null -> {
                throw EmptyResultDataAccessException(1)
            }
            else -> {
                AuditUser(keyHolder.key!!.toLong(), oidcUser)
            }
        }
    }
}
