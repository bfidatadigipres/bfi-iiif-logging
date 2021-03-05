CREATE TABLE `user`
(
    `id`  INT NOT NULL auto_increment,
    `sub` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `sub_unique` (`sub`)
);

CREATE TABLE `audit_event`
(
    `id`           INT NOT NULL auto_increment,
    `user_id`      INT NOT NULL,
    `created_date` DATETIME NOT NULL,
    `event_type`   VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_audit_event_user_idx` (`user_id`),
    CONSTRAINT `fk_audit_event_user` FOREIGN KEY (`user_id`) REFERENCES `user`
        (`id`)
);

CREATE TABLE `download_panel_opened`
(
    `id`             INT NOT NULL auto_increment,
    `audit_event_id` INT NOT NULL,
    `manifest`       VARCHAR(2048) NOT NULL,
    `canvas_id`      VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_download_panel_opened_audit_event_idx` (`audit_event_id`),
    CONSTRAINT `fk_download_panel_opened_audit_event` FOREIGN KEY (
                                                                   `audit_event_id`) REFERENCES `audit_event` (`id`)
);

CREATE TABLE `image_changed`
(
    `id`             INT NOT NULL auto_increment,
    `audit_event_id` INT NOT NULL,
    `manifest`       VARCHAR(2048) NOT NULL,
    `canvas_id`      VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_image_changed_audit_event_idx` (`audit_event_id`),
    CONSTRAINT `fk_image_changed_audit_event` FOREIGN KEY (`audit_event_id`)
        REFERENCES `audit_event` (`id`)
);

CREATE TABLE `resource_loaded`
(
    `id`             INT NOT NULL auto_increment,
    `audit_event_id` INT NOT NULL,
    `uri`            VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_resource_loaded_audit_event_idx` (`audit_event_id`),
    CONSTRAINT `fk_resource_loaded_audit_event` FOREIGN KEY (`audit_event_id`)
        REFERENCES `audit_event` (`id`)
);