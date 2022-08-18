package uk.bfi.uvaudit.event

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
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

    @ExceptionHandler(InvalidTypeIdException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleInvalidTypeIdException(ex: InvalidTypeIdException) {
        logger.error("Audit event structure was invalid", ex)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException) {
        logger.error("Audit event structure was invalid", ex)
    }

    @ExceptionHandler(EmailNotVerifiedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected fun handleEmailNotVerified(ex: EmailNotVerifiedException) {
        logger.error("Email address not verified", ex)
    }

    @PostMapping("/api/event")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun onEvent(@AuthenticationPrincipal user: AuditUser, @RequestBody event: AuditEvent) {
        validateUser(user)
        writer.write(user.id, event)
    }

    @GetMapping("/api/request")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun onRequest(
        @AuthenticationPrincipal user: AuditUser,
        @RequestHeader("X-API-RequestType") requestType: String,
        @RequestHeader("X-API-RequestURI") requestUri: String
    ) {
        validateUser(user)
        writer.write(user.id, requestType, requestUri)
    }

    @Throws(EmailNotVerifiedException::class)
    private fun validateUser(
        user: AuditUser
    ) {
        // FIXME
//        if (user.getAttribute<Boolean>("email_verified") != true) {
//            throw EmailNotVerifiedException()
//        }
    }
}
