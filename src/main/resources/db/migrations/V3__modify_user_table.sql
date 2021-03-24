ALTER TABLE `logging`.`user`
    ADD COLUMN `email` VARCHAR(320) NOT NULL after `sub`,
    ADD COLUMN `department` VARCHAR(100) NULL after `email`;
