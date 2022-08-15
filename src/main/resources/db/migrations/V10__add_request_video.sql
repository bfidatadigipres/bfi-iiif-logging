CREATE TABLE `request_video`
(
    `id`           INT NOT NULL auto_increment,
    `audit_log_id` INT NOT NULL,
    `video`        VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_request_video_audit_log_idx` (`audit_log_id`),
    CONSTRAINT `fk_request_video_audit_log` FOREIGN KEY (`audit_log_id`)
        REFERENCES `audit_log` (`id`)
);
