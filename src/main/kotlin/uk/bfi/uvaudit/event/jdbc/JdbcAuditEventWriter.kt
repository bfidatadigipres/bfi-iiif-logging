package uk.bfi.uvaudit.event.jdbc

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import uk.bfi.uvaudit.event.AuditEvent
import uk.bfi.uvaudit.event.AuditEventWriter
import java.util.*
import javax.sql.DataSource

class JdbcAuditEventWriter(dataSource: DataSource) : AuditEventWriter {

    private val template = NamedParameterJdbcTemplate(dataSource)

    override fun write(user: Long, event: AuditEvent) {
        val parentRecord = createAuditEventParentRecord(user, event)
        when (event) {
            is AuditEvent.DownloadPanelOpened -> writeDownloadPanelEvent(parentRecord, event)
            is AuditEvent.ImageChanged -> writeImageChangedEvent(parentRecord, event)
            is AuditEvent.ResourceLoaded -> writeResourceLoadedEvent(parentRecord, event)
        }
    }

    private fun createAuditEventParentRecord(userId: Long, event: AuditEvent): Long {
        val sql = """INSERT INTO audit_event
                (user_id, created_date, event_type)
            VALUES
                (:user_id, :created_date, :event_type)"""
        val params = MapSqlParameterSource(
            mapOf(
                "user_id" to userId,
                "created_date" to Date(),
                "event_type" to event.type
            )
        )
        val keyHolder = GeneratedKeyHolder()

        template.update(sql, params, keyHolder)

        return when (keyHolder.key) {
            null -> {
                throw EmptyResultDataAccessException(1)
            }
            else -> {
                keyHolder.key!!.toLong()
            }
        }
    }

    private fun writeDownloadPanelEvent(parentRecord: Long, event: AuditEvent.DownloadPanelOpened) {
        val sql = """
            INSERT INTO download_panel_opened
                (audit_event_id, manifest, canvas_id)
            VALUES
                (:audit_event_id, :manifest, :canvas_id)"""
        val params = MapSqlParameterSource(
            mapOf(
                "audit_event_id" to parentRecord,
                "manifest" to event.manifest,
                "canvas_id" to event.canvas
            )
        )

        template.update(sql, params)
    }

    private fun writeResourceLoadedEvent(parentRecord: Long, event: AuditEvent.ResourceLoaded) {
        val sql = """
            INSERT INTO resource_loaded
                (audit_event_id, uri)
            VALUES
                (:audit_event_id, :uri)"""
        val params = MapSqlParameterSource(
            mapOf(
                "audit_event_id" to parentRecord,
                "uri" to event.uri,
            )
        )

        template.update(sql, params)
    }

    private fun writeImageChangedEvent(parentRecord: Long, event: AuditEvent.ImageChanged) {
        val sql = """
            INSERT INTO image_changed
                (audit_event_id, manifest, canvas_id)
            VALUES
                (:audit_event_id, :manifest, :canvas_id)"""
        val params = MapSqlParameterSource(
            mapOf(
                "audit_event_id" to parentRecord,
                "manifest" to event.manifest,
                "canvas_id" to event.canvas
            )
        )

        template.update(sql, params)
    }
}
