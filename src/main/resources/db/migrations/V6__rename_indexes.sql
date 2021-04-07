ALTER TABLE `logging`.`audit_log`
    RENAME INDEX `fk_audit_event_user_idx` TO `fk_audit_log_user_idx`;
ALTER TABLE `logging`.`audit_log`
    ALTER INDEX `fk_audit_log_user_idx` VISIBLE;

ALTER TABLE `logging`.`event_download_panel_opened`
    RENAME INDEX `fk_download_panel_opened_audit_event_idx` TO `fk_event_download_panel_opened_audit_log_idx`;
ALTER TABLE `logging`.`event_download_panel_opened`
    ALTER INDEX `fk_event_download_panel_opened_audit_log_idx` VISIBLE;

ALTER TABLE `logging`.`event_image_changed`
    RENAME INDEX `fk_image_changed_audit_event_idx` TO `fk_event_image_changed_audit_log_idx`;
ALTER TABLE `logging`.`event_image_changed`
    ALTER INDEX `fk_event_image_changed_audit_log_idx` VISIBLE;

ALTER TABLE `logging`.`event_resource_loaded`
    RENAME INDEX `fk_resource_loaded_audit_event_idx` TO `fk_event_resource_loaded_audit_log_idx`;
ALTER TABLE `logging`.`event_resource_loaded`
    ALTER INDEX `fk_event_resource_loaded_audit_log_idx` VISIBLE;
