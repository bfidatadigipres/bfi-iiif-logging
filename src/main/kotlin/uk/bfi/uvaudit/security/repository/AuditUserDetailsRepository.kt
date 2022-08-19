package uk.bfi.uvaudit.security.repository

import org.springframework.dao.support.DataAccessUtils
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import uk.bfi.uvaudit.security.AuditUserDetails
import javax.sql.DataSource

class AuditUserDetailsRepository(private val dataSource: DataSource) {
    private val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)

    fun create(details: AuditUserDetails): Long {
        val sql = "INSERT INTO user (sub, email, department) VALUES (:sub, :email, :department)"
        val params = MapSqlParameterSource(
            mapOf(
                "sub" to details.subject,
                "email" to details.email,
                "department" to details.department
            )
        )
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(sql, params, keyHolder)

        return keyHolder.key!!.toLong()
    }

    fun lookup(details: AuditUserDetails): Long? {
        val sql = "SELECT id FROM user where sub = :sub"
        val params = MapSqlParameterSource(
            mapOf("sub" to details.subject)
        )

        val dbUserIds = jdbcTemplate.queryForList(sql, params, Long::class.java)
        return DataAccessUtils.uniqueResult(dbUserIds)
    }

    fun lookupOrCreate(details: AuditUserDetails) = lookup(details) ?: create(details)

    fun update(id: Long, details: AuditUserDetails) {
        val sql = "UPDATE user SET email = :email, department = :department WHERE id = :id"
        val params = MapSqlParameterSource(
            mapOf(
                "email" to details.email,
                "department" to details.department,
                "id" to id
            )
        )

        jdbcTemplate.update(sql, params)
    }
}
