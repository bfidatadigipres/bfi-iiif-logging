package uk.bfi.uvaudit.event

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
sealed class AuditEvent {

    abstract val type: String

    @JsonTypeName("download_panel_opened")
    data class DownloadPanelOpened(val manifest: String, val canvas: String) : AuditEvent() {
        override val type = "download_panel_opened"
    }

    @JsonTypeName("resource_loaded")
    data class ResourceLoaded(val uri: String) : AuditEvent() {
        override val type = "resource_loaded"
    }

    @JsonTypeName("image_changed")
    data class ImageChanged(val manifest: String, val canvas: String) : AuditEvent() {
        override val type = "image_changed"
    }
}
