package uk.bfi.uvaudit.event

/**
 * A mechanism for associating [AuditEvent]s with users and writing them back to
 * persistent storage.
 */
interface AuditEventWriter {

    fun write(userId: Long, auditEvent: AuditEvent)

    fun write(userId: Long, requestType: String, requestUri: String)
}
