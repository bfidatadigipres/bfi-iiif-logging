package uk.bfi.uvaudit.event.console

import mu.KotlinLogging
import uk.bfi.uvaudit.event.AuditEvent
import uk.bfi.uvaudit.event.AuditEventWriter

/**
 * A simple [AuditEventWriter] implementation that writes [AuditEvent]s to the console via SLF4J logging.
 */
class LoggingAuditEventWriter : AuditEventWriter {

    private val logger = KotlinLogging.logger {}

    override fun write(userId: Long, auditEvent: AuditEvent) {
        logger.info { "User $userId sent $auditEvent" }
    }

    override fun write(userId: Long, requestType: String, requestUri: String) {
        logger.info { "User $userId sent $requestType with URI $requestUri" }
    }
}
