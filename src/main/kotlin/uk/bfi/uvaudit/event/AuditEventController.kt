package uk.bfi.uvaudit.event

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import uk.bfi.uvaudit.security.AuditUser


@RestController
class AuditEventController(
    private val writer: AuditEventWriter
) {
    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException
    ) {
        logger.error("Audit event structure was invalid", ex)
    }

    @PostMapping("/api/event")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun onEvent(@AuthenticationPrincipal user: AuditUser, @RequestBody event: AuditEvent) {
        writer.write(user.id, event)
    }

}
