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
)
    engine=innodb
    DEFAULT charset=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;
