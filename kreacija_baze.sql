CREATE SCHEMA `projekt_kviz` ;

CREATE TABLE `projekt_kviz`.`tip_vprasanj` (
  `idtip_vprasanj` INT NOT NULL AUTO_INCREMENT,
  `tip_vprasanj` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idtip_vprasanj`));
  
CREATE TABLE `projekt_kviz`.`tezavnost` (
  `idtezavnost` INT NOT NULL AUTO_INCREMENT,
  `tezavnost` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idtezavnost`));
  
CREATE TABLE `projekt_kviz`.`kategorija` (
  `idkategorija` INT NOT NULL AUTO_INCREMENT,
  `kategorija` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idkategorija`));
  
CREATE TABLE `projekt_kviz`.`vprasanje` (
  `idvprasanje` INT NOT NULL AUTO_INCREMENT,
  `vprasanje` VARCHAR(2000) NOT NULL,
  `pravilen_odgovor` VARCHAR(1000) NOT NULL,
  `nepravilen_odgovor1` VARCHAR(1000) NOT NULL,
  `nepravilen_odgovor2` VARCHAR(1000),
  `nepravilen_odgovor3` VARCHAR(1000),
  `fk_tezavnost` INT NULL,
  `fk_kategorija` INT NULL,
  `fk_tip_vprasanj` INT NULL,
  PRIMARY KEY (`idvprasanje`),
  INDEX `fk_tezavnost_idx` (`fk_tezavnost` ASC) VISIBLE,
  INDEX `fk_kategorija_idx` (`fk_kategorija` ASC) VISIBLE,
  INDEX `fk_tip_vprasanj_idx` (`fk_tip_vprasanj` ASC) VISIBLE,
  CONSTRAINT `fk_tip_vprasanj`
    FOREIGN KEY (`fk_tip_vprasanj`)
    REFERENCES `projekt_kviz`.`tip_vprasanj` (`idtip_vprasanj`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_kategorija`
    FOREIGN KEY (`fk_kategorija`)
    REFERENCES `projekt_kviz`.`kategorija` (`idkategorija`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_tezavnost`
    FOREIGN KEY (`fk_tezavnost`)
    REFERENCES `projekt_kviz`.`tezavnost` (`idtezavnost`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
insert into tezavnost value (1, "easy");
insert into tezavnost value (2, "medium");
insert into tezavnost value (3, "hard");

insert into tip_vprasanj value (1, "multiple");
insert into tip_vprasanj value (2, "boolean");

insert into kategorija value (1, "sport");
insert into kategorija value (2, "General Knowledge");
insert into kategorija value (3, "Science: Computers");
insert into kategorija value (4, "Geography");
insert into kategorija value (6, "Entertainment: Film");
insert into kategorija value (5, "Entertainment: Music");