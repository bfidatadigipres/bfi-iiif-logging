package uk.bfi.uvaudit.event

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class AuditEventController {
    private val logger = KotlinLogging.logger {}

    @PostMapping("/api/event")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun onEvent(@RequestBody event: AuditEvent) {
        logger.info { "Received event: ${event}" }
    }

}
