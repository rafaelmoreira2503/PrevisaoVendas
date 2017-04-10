SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `confiabilidade` DEFAULT CHARACTER SET utf8 ;
USE `confiabilidade` ;

-- -----------------------------------------------------
-- Table `confiabilidade`.`area`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`area` (
  `codigo` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(50) NOT NULL,
  `version` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `confiabilidade`.`empresa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`empresa` (
  `codigo` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ativa` TINYINT(1) NULL DEFAULT NULL,
  `cnpj` VARCHAR(18) NOT NULL,
  `data_fim` DATE NOT NULL,
  `data_ini` DATE NOT NULL,
  `bairro` VARCHAR(30) NULL DEFAULT NULL,
  `cep` VARCHAR(10) NULL DEFAULT NULL,
  `cidade` VARCHAR(30) NULL DEFAULT NULL,
  `complemento` VARCHAR(30) NULL DEFAULT NULL,
  `logradouro` VARCHAR(50) NULL DEFAULT NULL,
  `numero` VARCHAR(5) NULL DEFAULT NULL,
  `uf` VARCHAR(2) NULL DEFAULT NULL,
  `iscricao_estadual` VARCHAR(14) NOT NULL,
  `nome` VARCHAR(50) NOT NULL,
  `nome_fantasia` VARCHAR(30) NOT NULL,
  `segmento` VARCHAR(15) NOT NULL,
  `sponsor` VARCHAR(40) NOT NULL,
  `totalItems` INT(11) NOT NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `confiabilidade`.`especialidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`especialidade` (
  `codigo` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(30) NOT NULL,
  `version` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `confiabilidade`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`usuario` (
  `codigo` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(60) NOT NULL,
  `nome` VARCHAR(60) NOT NULL,
  `permissao` VARCHAR(15) NOT NULL,
  `senha` VARCHAR(255) NOT NULL,
  `tel_celular` VARCHAR(15) NOT NULL,
  `tel_fixo` VARCHAR(14) NULL DEFAULT NULL,
  `ramal` VARCHAR(4) NULL DEFAULT NULL,
  `version` INT(11) NULL DEFAULT NULL,
  `codigo_area` BIGINT(20) NULL DEFAULT NULL,
  `codigo_chefe` BIGINT(20) NULL DEFAULT NULL,
  `codigo_empresa` BIGINT(20) NULL DEFAULT NULL,
  `codigo_especialidade` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE INDEX `UK_5171l57faosmj8myawaucatdw` (`email` ASC),
  INDEX `FK_8wcgn5fdagoaqj0vv95lw8pru` (`codigo_area` ASC),
  INDEX `FK_d1j6ju99o2ynv0fwj7l7afcen` (`codigo_chefe` ASC),
  INDEX `FK_623gjryt5ert8f1dck1rd16m5` (`codigo_empresa` ASC),
  INDEX `FK_tp15605o16kq5snivh4to0uii` (`codigo_especialidade` ASC),
  CONSTRAINT `FK_623gjryt5ert8f1dck1rd16m5`
    FOREIGN KEY (`codigo_empresa`)
    REFERENCES `confiabilidade`.`empresa` (`codigo`),
  CONSTRAINT `FK_8wcgn5fdagoaqj0vv95lw8pru`
    FOREIGN KEY (`codigo_area`)
    REFERENCES `confiabilidade`.`area` (`codigo`),
  CONSTRAINT `FK_d1j6ju99o2ynv0fwj7l7afcen`
    FOREIGN KEY (`codigo_chefe`)
    REFERENCES `confiabilidade`.`usuario` (`codigo`),
  CONSTRAINT `FK_tp15605o16kq5snivh4to0uii`
    FOREIGN KEY (`codigo_especialidade`)
    REFERENCES `confiabilidade`.`especialidade` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `confiabilidade`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`item` (
  `codigo` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `codigo_item` VARCHAR(20) NOT NULL,
  `criticidade` VARCHAR(1) NULL DEFAULT NULL,
  `data_item` DATE NULL DEFAULT NULL,
  `descricao` VARCHAR(500) NOT NULL,
  `justificativa` VARCHAR(40) NULL DEFAULT NULL,
  `naoAplicavel` TINYINT(1) NULL DEFAULT NULL,
  `status` VARCHAR(40) NULL DEFAULT NULL,
  `codigo_area` BIGINT(20) NULL DEFAULT NULL,
  `codigo_empresa` BIGINT(20) NULL DEFAULT NULL,
  `codigo_tecnico` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  INDEX `FK_ln00i3xh69d8qkea8bk1jh6cr` (`codigo_area` ASC),
  INDEX `FK_apbkkcg5dp9fbi7l0nyg9sjjc` (`codigo_empresa` ASC),
  INDEX `FK_ckbndspuxjivrwrv9bq41cpsg` (`codigo_tecnico` ASC),
  CONSTRAINT `FK_apbkkcg5dp9fbi7l0nyg9sjjc`
    FOREIGN KEY (`codigo_empresa`)
    REFERENCES `confiabilidade`.`empresa` (`codigo`),
  CONSTRAINT `FK_ckbndspuxjivrwrv9bq41cpsg`
    FOREIGN KEY (`codigo_tecnico`)
    REFERENCES `confiabilidade`.`usuario` (`codigo`),
  CONSTRAINT `FK_ln00i3xh69d8qkea8bk1jh6cr`
    FOREIGN KEY (`codigo_area`)
    REFERENCES `confiabilidade`.`area` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 469
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `confiabilidade`.`pergunta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`pergunta` (
  `codigo` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(60) NOT NULL,
  `version` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `confiabilidade`.`pergunta_empresa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`pergunta_empresa` (
  `codigo` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `resposta` VARCHAR(40) NULL DEFAULT NULL,
  `codigo_area` BIGINT(20) NULL DEFAULT NULL,
  `codigo_empresa` BIGINT(20) NOT NULL,
  `codigo_pergunta` BIGINT(20) NOT NULL,
  `codigo_item` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  INDEX `FK_86ywc35rl73xyibn4xhe6bk4` (`codigo_area` ASC),
  INDEX `FK_a87fxfrs8ick9dkliwuglenf7` (`codigo_empresa` ASC),
  INDEX `FK_t6kthbtl5bxfvnkp610vmshex` (`codigo_pergunta` ASC),
  INDEX `FK_tlmvl6h3p3d1ucxynvu2awlrt` (`codigo_item` ASC),
  CONSTRAINT `FK_86ywc35rl73xyibn4xhe6bk4`
    FOREIGN KEY (`codigo_area`)
    REFERENCES `confiabilidade`.`area` (`codigo`),
  CONSTRAINT `FK_a87fxfrs8ick9dkliwuglenf7`
    FOREIGN KEY (`codigo_empresa`)
    REFERENCES `confiabilidade`.`empresa` (`codigo`),
  CONSTRAINT `FK_t6kthbtl5bxfvnkp610vmshex`
    FOREIGN KEY (`codigo_pergunta`)
    REFERENCES `confiabilidade`.`pergunta` (`codigo`),
  CONSTRAINT `FK_tlmvl6h3p3d1ucxynvu2awlrt`
    FOREIGN KEY (`codigo_item`)
    REFERENCES `confiabilidade`.`item` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `confiabilidade`.`pergunta_empresa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `confiabilidade`.`itemabc` (
  `codigo` bigint(20) NOT NULL AUTO_INCREMENT,
  `abc` varchar(255) DEFAULT NULL,
  `codigo_Abc` varchar(20) NOT NULL,
  `data_Abc` date DEFAULT NULL,
  `descricaoAbc` varchar(255) DEFAULT NULL,
  `media` double DEFAULT NULL,
  `percentagem` int(11) DEFAULT NULL,
  `percentual` double DEFAULT NULL,
  `codigo_empresaAbc` bigint(20) DEFAULT NULL,
  `codigo_itemNoAbc` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_1j866g3acxatduc4embsc5pt2` (`codigo_empresaAbc`),
  KEY `FK_heu9are93r6pqyjekt8u47e6d` (`codigo_itemNoAbc`),
  CONSTRAINT `FK_1j866g3acxatduc4embsc5pt2` FOREIGN KEY (`codigo_empresaAbc`) REFERENCES `empresa` (`codigo`),
  CONSTRAINT `FK_heu9are93r6pqyjekt8u47e6d` FOREIGN KEY (`codigo_itemNoAbc`) REFERENCES `item` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=9835 DEFAULT CHARSET=utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
