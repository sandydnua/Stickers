CREATE TABLE IF NOT EXISTS `stickers`.`operations_for_groups` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `operation` INT NOT NULL,
  `group` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_operations_for_groups_groups1_idx` (`group` ASC),
  INDEX `fk_operations_for_groups_operations1_idx` (`operation` ASC),
  UNIQUE INDEX `unique` (`operation` ASC, `group` ASC),
  CONSTRAINT `fk_operations_for_groups_operations1`
    FOREIGN KEY (`operation`)
    REFERENCES `stickers`.`operations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_operations_for_groups_groups1`
    FOREIGN KEY (`group`)
    REFERENCES `stickers`.`groups` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB