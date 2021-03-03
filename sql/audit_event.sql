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
)
    engine=innodb
    DEFAULT charset=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;
