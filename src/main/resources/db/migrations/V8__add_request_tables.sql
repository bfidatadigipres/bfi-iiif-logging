CREATE TABLE `request_image`
(
    `id`           INT NOT NULL auto_increment,
    `audit_log_id` INT NOT NULL,
    `image`        VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_request_image_audit_log_idx` (`audit_log_id`),
    CONSTRAINT `fk_request_image_audit_log` FOREIGN KEY (`audit_log_id`)
        REFERENCES `audit_log` (`id`)
);

CREATE TABLE `request_manifest`
(
    `id`           INT NOT NULL auto_increment,
    `audit_log_id` INT NOT NULL,
    `manifest`     VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_request_manifest_audit_log_idx` (`audit_log_id`),
    CONSTRAINT `fk_request_manifest_audit_log` FOREIGN KEY (`audit_log_id`)
        REFERENCES `audit_log` (`id`)
);
