ALTER TABLE `logging`.`audit_log`
    CHANGE COLUMN `event_type` `log_type` VARCHAR(21) NOT NULL ;
