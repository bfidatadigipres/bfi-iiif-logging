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

    override fun write(userId: Long, auditEvent: AuditEvent) {
        val auditLogId = createAndGetAuditLogId(userId, "event_${auditEvent.type}")
        when (auditEvent) {
            is AuditEvent.DownloadPanelOpened -> writeDownloadPanelEvent(auditLogId, auditEvent)
            is AuditEvent.ImageChanged -> writeImageChangedEvent(auditLogId, auditEvent)
            is AuditEvent.ResourceLoaded -> writeResourceLoadedEvent(auditLogId, auditEvent)
        }
    }

    override fun write(userId: Long, requestType: String, requestUri: String) {
        val auditLogId = createAndGetAuditLogId(userId, "request_$requestType");
        when (requestType) {
            "manifest" -> writeManifestRequest(auditLogId, requestUri)
            "image" -> writeImageRequest(auditLogId, requestUri)
            else -> throw Exception("Unexpected request type [$requestType]")
        }
    }

    private fun createAndGetAuditLogId(userId: Long, logType: String): Long {
        val sql = """
            INSERT INTO audit_log
                (user_id, created_date, log_type)
            VALUES
                (:user_id, :created_date, :log_type)"""
        val params = MapSqlParameterSource(
            mapOf(
                "user_id" to userId,
                "created_date" to Date(),
                "log_type" to logType
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

    private fun writeDownloadPanelEvent(auditLogId: Long, auditEvent: AuditEvent.DownloadPanelOpened) {
        val sql = """
            INSERT INTO event_download_panel_opened
                (audit_log_id, manifest, canvas_id)
            VALUES
                (:audit_log_id, :manifest, :canvas_id)"""
        val params = MapSqlParameterSource(
            mapOf(
                "audit_log_id" to auditLogId,
                "manifest" to auditEvent.manifest,
                "canvas_id" to auditEvent.canvas
            )
        )

        template.update(sql, params)
    }

    private fun writeResourceLoadedEvent(auditLogId: Long, auditEvent: AuditEvent.ResourceLoaded) {
        val sql = """
            INSERT INTO event_resource_loaded
                (audit_log_id, uri)
            VALUES
                (:audit_log_id, :uri)"""
        val params = MapSqlParameterSource(
            mapOf(
                "audit_log_id" to auditLogId,
                "uri" to auditEvent.uri,
            )
        )

        template.update(sql, params)
    }

    private fun writeImageChangedEvent(auditLogId: Long, auditEvent: AuditEvent.ImageChanged) {
        val sql = """
            INSERT INTO event_image_changed
                (audit_log_id, manifest, canvas_id)
            VALUES
                (:audit_log_id, :manifest, :canvas_id)"""
        val params = MapSqlParameterSource(
            mapOf(
                "audit_log_id" to auditLogId,
                "manifest" to auditEvent.manifest,
                "canvas_id" to auditEvent.canvas
            )
        )

        template.update(sql, params)
    }

    private fun writeManifestRequest(auditLogId: Long, manifest: String) {
        val sql = """
            INSERT INTO request_manifest
                (audit_log_id, manifest)
            VALUES
                (:audit_log_id, :manifest)
        """
        val params = MapSqlParameterSource(
            mapOf(
                "audit_log_id" to auditLogId,
                "manifest" to manifest
            )
        )

        template.update(sql, params)
    }

    private fun writeImageRequest(auditLogId: Long, image: String) {
        val sql = """
            INSERT INTO request_image
                (audit_log_id, image)
            VALUES
                (:audit_log_id, :image)
        """
        val params = MapSqlParameterSource(
            mapOf(
                "audit_log_id" to auditLogId,
                "image" to image
            )
        )

        template.update(sql, params)
    }
}
