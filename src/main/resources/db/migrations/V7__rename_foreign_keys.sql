ALTER TABLE `logging`.`audit_log`
    DROP FOREIGN KEY `fk_audit_event_user`;
ALTER TABLE `logging`.`audit_log`
    ADD CONSTRAINT `fk_audit_log_user` FOREIGN KEY (`user_id`)
        REFERENCES `logging`.`user` (`id`);

ALTER TABLE `logging`.`event_download_panel_opened`
    DROP FOREIGN KEY `fk_download_panel_opened_audit_event`;
ALTER TABLE `logging`.`event_download_panel_opened`
    CHANGE COLUMN `audit_event_id` `audit_log_id` INT NOT NULL;
ALTER TABLE `logging`.`event_download_panel_opened`
    ADD CONSTRAINT `fk_event_download_panel_opened_audit_log` FOREIGN KEY (`audit_log_id`)
        REFERENCES `logging`.`audit_log` (`id`);

ALTER TABLE `logging`.`event_image_changed`
    DROP FOREIGN KEY `fk_image_changed_audit_event`;
ALTER TABLE `logging`.`event_image_changed`
    CHANGE COLUMN `audit_event_id` `audit_log_id` INT NOT NULL;
ALTER TABLE `logging`.`event_image_changed`
    ADD CONSTRAINT `fk_event_image_changed_audit_log` FOREIGN KEY (`audit_log_id`)
        REFERENCES `logging`.`audit_log` (`id`);

ALTER TABLE `logging`.`event_resource_loaded`
    DROP FOREIGN KEY `fk_resource_loaded_audit_event`;
ALTER TABLE `logging`.`event_resource_loaded`
    CHANGE COLUMN `audit_event_id` `audit_log_id` INT NOT NULL;
ALTER TABLE `logging`.`event_resource_loaded`
    ADD CONSTRAINT `fk_event_resource_loaded_audit_log` FOREIGN KEY (`audit_log_id`)
        REFERENCES `logging`.`audit_log` (`id`);
