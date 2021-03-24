ALTER TABLE `logging`.`audit_event`
    RENAME TO `logging`.`audit_log`;

ALTER TABLE `logging`.`download_panel_opened`
    RENAME TO `logging`.`event_download_panel_opened`;

ALTER TABLE `logging`.`image_changed`
    RENAME TO `logging`.`event_image_changed`;

ALTER TABLE `logging`.`resource_loaded`
    RENAME TO `logging`.`event_resource_loaded`;
