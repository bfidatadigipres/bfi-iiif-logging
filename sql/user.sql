CREATE TABLE `user`
(
    `id`  INT NOT NULL auto_increment,
    `sub` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `sub_unique` (`sub`)
)
    engine=innodb
    DEFAULT charset=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;
