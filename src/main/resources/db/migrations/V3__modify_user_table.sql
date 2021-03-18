ALTER TABLE `logging`.`user`
    ADD COLUMN `email` VARCHAR(320) NOT NULL AFTER `sub`,
    ADD COLUMN `department` VARCHAR(100) NULL AFTER `email`;
