-- MySQL Script generated by MySQL Workbench
-- Sat Mar  4 16:25:08 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering
-- -----------------------------------------------------
-- Schema editorialdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `editorialdb`;
-- -----------------------------------------------------
-- Table `editorialdb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `editorialdb`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(75) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`));
-- -----------------------------------------------------
-- Table `editorialdb`.`article_correct`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `editorialdb`.`article_correct` (
  `id_correct` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `date_of_correction` DATETIME NULL,
  `is_corrected` TINYINT(1) NOT NULL,
  `journalist_id_c` BIGINT NOT NULL,
  `corrector_id_c` BIGINT NULL,
  PRIMARY KEY (`id_correct`),
  INDEX `article_fk_idx` (`corrector_id_c` ASC),
  CONSTRAINT `article_fk`
    FOREIGN KEY (`corrector_id_c`)
    REFERENCES `editorialdb`.`user` (`id`));
-- -----------------------------------------------------
-- Table `editorialdb`.`article_draft`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `editorialdb`.`article_draft` (
  `id_draft` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `date_of_update` DATETIME NOT NULL,
  `journalist_id_d` BIGINT NOT NULL,
  PRIMARY KEY (`id_draft`),
  INDEX `draft_fk_idx` (`journalist_id_d` ASC),
  CONSTRAINT `draft_fk`
    FOREIGN KEY (`journalist_id_d`)
    REFERENCES `editorialdb`.`user` (`id`)
	ON DELETE CASCADE ON UPDATE CASCADE);
-- -----------------------------------------------------
-- Table `editorialdb`.`article_proposal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `editorialdb`.`article_proposal` (
  `id_proposal` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `acceptance` ENUM('PENDING', 'ACCEPTED', 'DECLINED') NOT NULL,
  `date_of_update` DATETIME NOT NULL,
  `journalist_id_p` BIGINT NOT NULL,
  PRIMARY KEY (`id_proposal`),
  INDEX `redactor_id_fk_idx` (`journalist_id_p` ASC),
  CONSTRAINT `redactor_id_fk`
    FOREIGN KEY (`journalist_id_p`)
    REFERENCES `editorialdb`.`user` (`id`)
	ON DELETE CASCADE ON UPDATE CASCADE);
-- -----------------------------------------------------
-- Table `editorialdb`.`authority`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `editorialdb`.`authority` (
  `user_id_a` BIGINT NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  INDEX `authority_fk` (`user_id_a` ASC),
  CONSTRAINT `authority_fk`
    FOREIGN KEY (`user_id_a`)
    REFERENCES `editorialdb`.`user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE);
-- -----------------------------------------------------
-- Table `editorialdb`.`user_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `editorialdb`.`user_detail` (
  `user_id_d` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `supplier` VARCHAR(30) NOT NULL,
  INDEX `user_id_fk_idx` (`user_id_d` ASC),
  CONSTRAINT `user_id_fk`
    FOREIGN KEY (`user_id_d`)
    REFERENCES `editorialdb`.`user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE);
	
LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'user','$2a$12$iD9Psi.mmFcVXrvbeiI7Juiz5lDPn07Ska21Cnv21AgIHbIUE355m',1);
INSERT INTO `user` VALUES (2,'user2','$2a$12$5b36FpgFqvc7826RWQpBxObz0oQAxadnVCwgJ43XEGhJJrrcs/mli',1);
INSERT INTO `user` VALUES (3,'user3','$2a$12$nbrlhOxErstHKvSJyjzJ1uCAhItuoukWXlyYXWiTqf/Z/QSNPNP8O',1);
INSERT INTO `user` VALUES (4,'user4','$2a$12$CV0QLaHzkp.O2PEIP7GdHOm/1ykl2ySBS3RSqKl2OBgGAJE87DpRe',1);
INSERT INTO `user` VALUES (5,'user5','$2a$12$ZIDwaTNwptT8Sa09/ZywruvOunIBmBChY6Jd3yIvetidOHvwjIK3u',1);
INSERT INTO `user` VALUES (6,'user6','$2a$12$ET.YNwoMCVYueEK1hhYd3.rIGLGvjIHAj2mINIjmuIAK3C5hyKh.e',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `user_detail` WRITE;
/*!40000 ALTER TABLE `user_detail` DISABLE KEYS */;
INSERT INTO `user_detail` VALUES (1,'UserName','UserSurname','UserEmail@email.com','APP');
INSERT INTO `user_detail` VALUES (2,'UserName','UserSurname','UserEmail@email.com','APP');
INSERT INTO `user_detail` VALUES (3,'UserName','UserSurname','UserEmail@email.com','APP');
INSERT INTO `user_detail` VALUES (4,'UserNameJourn','UserSurnameJourn','UserEmailJourn@email.com','APP');
INSERT INTO `user_detail` VALUES (5,'UserNameRed','UserSurnameRed','UserEmailRed@email.com','APP');
INSERT INTO `user_detail` VALUES (6,'UserNameCor','UserSurnameCor','UserEmailCor@email.com','APP');
/*!40000 ALTER TABLE `user_detail` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES (1,'USER');
INSERT INTO `authority` VALUES (2,'ADMIN');
INSERT INTO `authority` VALUES (3,'USER');
INSERT INTO `authority` VALUES (4,'JOURNALIST');
INSERT INTO `authority` VALUES (5,'REDACTOR');
INSERT INTO `authority` VALUES (6,'CORRECTOR');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `article_correct` WRITE;
/*!40000 ALTER TABLE `article_correct` DISABLE KEYS */;
INSERT INTO `article_correct` VALUES (1,'Sektret sukcesu KFC','<h1>KFC (Kentucky Fried Chicken) </h1><p>Jest to amerykańska sieć restauracji fast food, która specjalizuje się w przysmakach z kurczaka. Jest to jedna z największych sieci fast food na świecie, z ponad 24 000 restauracji w ponad 145 krajach.</p><p>Historia KFC sięga lat 30. XX wieku, kiedy to Harland Sanders, zwany \"Colonel Sanders\", zaczął sprzedawać swoje chrupiące kawałki kurczaka w swojej restauracji w Corbin, w stanie Kentucky. Jego pomysł na smażonego kurczaka w ciekawym panierce odniósł sukces, a Sanders zaczął udzielać franczyzy innym restauracjom, co przyczyniło się do rozwoju sieci KFC.</p><p><img src=\"https://pbs.twimg.com/media/E1P8m_3XsAIoyht.jpg\" alt=\"Janusz Korwin-Mikke on Twitter: &quot;Pilne‼️Jonasz Koran-Mekka?? i Ozjasz  Goldberg?? są głęboko zaniepokojeni ostatnimi wydarzeniami w  Izraelu/Palestynie. Dlatego zapraszają na dzisiejszy live chat o 21.30 na  https://t.co/NIh6wrvnw7 #Korwin #Izrael ...\"></p><p><br></p><h2>Menu</h2><p>Obecnie, KFC oferuje szeroki wybór dań z kurczaka, takich jak chrupiące kawałki, skrzydełka, filety, zrazy i wiele innych. Oprócz tego, w menu można znaleźć frytki, sałatki, napoje i desery.</p><p>KFC stawia na jakość swojego jedzenia, dlatego wszystkie kawałki kurczaka są przygotowywane na bieżąco i smażone w oleju roślinnym, który nie zawiera szkodliwych substancji. Restauracje KFC oferują także opcje dla wegetarian, w tym sałatki i burger z wegetariańskim kotlecikiem.</p><p><br></p><h2>Kampanie reklamowe</h2><p>KFC jest znane nie tylko z pysznego jedzenia, ale także z innowacyjnych kampanii reklamowych. W 2019 roku, KFC wprowadziło na rynek wegańskiego kurczaka, co wywołało spore zainteresowanie wśród konsumentów. Restauracja wprowadziła również program KFC Colonel Scholars, który zapewnia stypendia dla studentów na studiach licencjackich w USA.</p><p><br></p><h2>Kontrowersje</h2><p>Jednakże, KFC nie jest pozbawione kontrowersji. W niektórych krajach, restauracje KFC są krytykowane za swoją szkodliwość dla zdrowia ze względu na dużą ilość tłuszczu i kalorii w oferowanych posiłkach. W 2003 roku, KFC zostało również oskarżone o wykorzystywanie pracowników zmuszanych do pracy w niehumanitarnych warunkach.</p><p>Mimo to, KFC pozostaje popularną siecią restauracji fast food na całym świecie. Z pewnością, smak chrupiącego kurczaka pozostanie ulubionym daniem wielu osób na długo.</p>','2023-03-20 00:00:00', 1,4,6 );
INSERT INTO `article_correct` VALUES (2,'Sektret sukcesu KFC','<h1>KFC (Kentucky Fried Chicken) </h1><p>Jest to amerykańska sieć restauracji fast food, która specjalizuje się w przysmakach z kurczaka. Jest to jedna z największych sieci fast food na świecie, z ponad 24 000 restauracji w ponad 145 krajach.</p><p>Historia KFC sięga lat 30. XX wieku, kiedy to Harland Sanders, zwany \"Colonel Sanders\", zaczął sprzedawać swoje chrupiące kawałki kurczaka w swojej restauracji w Corbin, w stanie Kentucky. Jego pomysł na smażonego kurczaka w ciekawym panierce odniósł sukces, a Sanders zaczął udzielać franczyzy innym restauracjom, co przyczyniło się do rozwoju sieci KFC.</p><p><img src=\"https://pbs.twimg.com/media/E1P8m_3XsAIoyht.jpg\" alt=\"Janusz Korwin-Mikke on Twitter: &quot;Pilne‼️Jonasz Koran-Mekka?? i Ozjasz  Goldberg?? są głęboko zaniepokojeni ostatnimi wydarzeniami w  Izraelu/Palestynie. Dlatego zapraszają na dzisiejszy live chat o 21.30 na  https://t.co/NIh6wrvnw7 #Korwin #Izrael ...\"></p><p><br></p><h2>Menu</h2><p>Obecnie, KFC oferuje szeroki wybór dań z kurczaka, takich jak chrupiące kawałki, skrzydełka, filety, zrazy i wiele innych. Oprócz tego, w menu można znaleźć frytki, sałatki, napoje i desery.</p><p>KFC stawia na jakość swojego jedzenia, dlatego wszystkie kawałki kurczaka są przygotowywane na bieżąco i smażone w oleju roślinnym, który nie zawiera szkodliwych substancji. Restauracje KFC oferują także opcje dla wegetarian, w tym sałatki i burger z wegetariańskim kotlecikiem.</p><p><br></p><h2>Kampanie reklamowe</h2><p>KFC jest znane nie tylko z pysznego jedzenia, ale także z innowacyjnych kampanii reklamowych. W 2019 roku, KFC wprowadziło na rynek wegańskiego kurczaka, co wywołało spore zainteresowanie wśród konsumentów. Restauracja wprowadziła również program KFC Colonel Scholars, który zapewnia stypendia dla studentów na studiach licencjackich w USA.</p><p><br></p><h2>Kontrowersje</h2><p>Jednakże, KFC nie jest pozbawione kontrowersji. W niektórych krajach, restauracje KFC są krytykowane za swoją szkodliwość dla zdrowia ze względu na dużą ilość tłuszczu i kalorii w oferowanych posiłkach. W 2003 roku, KFC zostało również oskarżone o wykorzystywanie pracowników zmuszanych do pracy w niehumanitarnych warunkach.</p><p>Mimo to, KFC pozostaje popularną siecią restauracji fast food na całym świecie. Z pewnością, smak chrupiącego kurczaka pozostanie ulubionym daniem wielu osób na długo.</p>','2023-03-20 00:00:00', 0,4,null);
/*!40000 ALTER TABLE `article_correct` ENABLE KEYS */;
UNLOCK TABLES;