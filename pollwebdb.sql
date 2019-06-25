﻿﻿﻿﻿/* SQL Manager for MySQL                              5.7.2.52112 */
/* -------------------------------------------------------------- */
/* Host     : localhost                                           */
/* Port     : 3306                                                */
/* Database : pollwebdb                                           */


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES 'utf8' */;


DROP DATABASE IF EXISTS `pollwebdb`;

CREATE DATABASE `pollwebdb`
    CHARACTER SET 'utf8'
    COLLATE 'utf8_general_ci';

CREATE USER website@localhost IDENTIFIED BY 'webpass';
GRANT ALL ON pollwebdb.* TO website@localhost;
USE `pollwebdb`;

/* Dropping database objects */

DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `poll_answer`;
DROP TABLE IF EXISTS `poll`;
DROP TABLE IF EXISTS `manager`;

/* Structure for the `manager` table : */

CREATE TABLE `manager` (
  `ID` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(20) COLLATE utf8_general_ci NOT NULL,
  `password` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY USING BTREE (`ID`),
  UNIQUE KEY `email` USING BTREE (`email`)
) ENGINE=InnoDB
ROW_FORMAT=DYNAMIC CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'
;

/* Structure for the `poll` table : */

CREATE TABLE `poll` (
  `ID` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `managerID` INTEGER(11) DEFAULT NULL,
  `title` VARCHAR(100) COLLATE utf8_general_ci DEFAULT NULL,
  `open_tag` TEXT COLLATE utf8_general_ci,
  `close_tag` TEXT COLLATE utf8_general_ci,
  `isReserved` TINYINT(1) DEFAULT 0,
  PRIMARY KEY USING BTREE (`ID`)
) ENGINE=InnoDB
ROW_FORMAT=DYNAMIC CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'
;

/* Structure for the `poll_answer` table : */

CREATE TABLE `poll_answer` (
  `ID` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `poll_ID` INTEGER(11) DEFAULT NULL,
  `user_ID` INTEGER(11) DEFAULT NULL,
  `answers` BLOB,
  PRIMARY KEY USING BTREE (`ID`)
) ENGINE=InnoDB
ROW_FORMAT=DYNAMIC CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'
;

/* Structure for the `question` table : */

CREATE TABLE `question` (
  `ID` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `poll_ID` INTEGER(11) NOT NULL,
  `type` ENUM('short_text','long_text','number','date','single_choice','multiple_choice') COLLATE utf8_general_ci NOT NULL DEFAULT 'short_text',
  `isMandatory` TINYINT(1) DEFAULT 0,
  `text` TEXT COLLATE utf8_general_ci,
  `answer` TEXT DEFAULT NULL,
  `note` TEXT COLLATE utf8_general_ci,
  `position` INTEGER(11) NOT NULL COMMENT 'position of question in a poll',
  PRIMARY KEY USING BTREE (`ID`)
) ENGINE=InnoDB
ROW_FORMAT=DYNAMIC CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'
;

/* Structure for the `user` table : */

CREATE TABLE `user` (
  `ID` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(20) COLLATE utf8_general_ci NOT NULL,
  `password` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
  `poll_ID` INTEGER(10) NOT NULL,
  PRIMARY KEY USING BTREE (`ID`),
  UNIQUE KEY `password` USING BTREE (`password`)
) ENGINE=InnoDB
ROW_FORMAT=DYNAMIC CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'
;
LOCK TABLES `poll` WRITE;
INSERT INTO `poll` (`managerID`, `title`,`open_tag`,`close_tag`,`isReserved`) VALUES (2,'poll 1','hello','bye',0),(2,'poll 2','hello world','bye world',1),(3,'poll 3','Poll about hobbies','the end',0);
UNLOCK TABLES;

LOCK TABLES `manager` WRITE;
INSERT INTO `manager` (`email`, `password`) VALUES ("admin@admin.com", "admin"),('manager@gmail.com','manager'),('manager2@gmail.com','manager2');
UNLOCK TABLES;

LOCK TABLES `user` WRITE;
INSERT INTO `user` (`email`, `password`,`poll_ID`) VALUES ('tani-dan@ukr.net','12345',2),('abr@ukr.net','qwert',1);
UNLOCK TABLES;

LOCK TABLES `question` WRITE;
INSERT INTO `question` (`poll_ID`, `type`,`isMandatory`,`text`,`answer`,`note`,`position`) VALUES (1,'date',0,'Select your date of birth',null,null,1), (1,'multiple_choice',0,'Select your interests','Sport,Cinema,Museum,Party',null,2),(2,'short_text',1,'What is your favourite color?',null,null,1),
                                                                                                  (3,'number',0,'What is your favourite number?',null,'it\'s a note',1);
UNLOCK TABLES;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;