package uk.bfi.uvaudit.event

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
sealed class AuditEvent  {
    @JsonTypeName("canvas_viewed")
    data class CanvasChanged(val id: String) : AuditEvent()
}

