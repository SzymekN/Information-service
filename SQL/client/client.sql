-- MySQL Script generated by MySQL Workbench
-- Sat Mar  4 16:28:10 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering
-- -----------------------------------------------------
-- Schema userdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `userdb`;
-- -----------------------------------------------------
-- Table `userdb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `userdb`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(75) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`));
-- -----------------------------------------------------
-- Table `userdb`.`article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `userdb`.`article` (
  `id_article` BIGINT NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `journalist_id_a` BIGINT NOT NULL,
  `article_content` LONGTEXT NOT NULL,
  `category` VARCHAR(200) NOT NULL,
  `date_of_submission` DATETIME NOT NULL,
  PRIMARY KEY (`id_article`),
  INDEX `owner_id_fk_idx` (`journalist_id_a` ASC),
  CONSTRAINT `owner_id_fk`
    FOREIGN KEY (`journalist_id_a`)
    REFERENCES `userdb`.`user` (`id`)
	ON DELETE CASCADE ON UPDATE CASCADE);
-- -----------------------------------------------------
-- Table `userdb`.`authority`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `userdb`.`authority` (
  `user_id_a` BIGINT NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  INDEX `role_id_fk_idx` (`user_id_a` ASC),
  CONSTRAINT `role_id_fk`
    FOREIGN KEY (`user_id_a`)
    REFERENCES `userdb`.`user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE);
-- -----------------------------------------------------
-- Table `userdb`.`endorsement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `userdb`.`endorsement` (
  `fk_article` BIGINT NOT NULL,
  `fk_user` BIGINT NOT NULL,
  `endorsement_value` TINYINT(1) NOT NULL,
  INDEX `fk_article_key_idx` (`fk_article` ASC),
  INDEX `fk_user_owner_key_idx` (`fk_user` ASC),
  CONSTRAINT `fk_article_key`
    FOREIGN KEY (`fk_article`)
    REFERENCES `userdb`.`article` (`id_article`),
  CONSTRAINT `fk_user_owner_key`
    FOREIGN KEY (`fk_user`)
    REFERENCES `userdb`.`user` (`id`)
	ON DELETE CASCADE ON UPDATE CASCADE);
-- -----------------------------------------------------
-- Table `userdb`.`user_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `userdb`.`user_detail` (
  `user_id_d` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `supplier` VARCHAR(30) NOT NULL,
  INDEX `user_id_fk_idx` (`user_id_d` ASC),
  CONSTRAINT `user_id_fk`
    FOREIGN KEY (`user_id_d`)
    REFERENCES `userdb`.`user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE);

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'user','$2a$12$iD9Psi.mmFcVXrvbeiI7Juiz5lDPn07Ska21Cnv21AgIHbIUE355m',1);
INSERT INTO `user` VALUES (2,'user2','$2a$12$5b36FpgFqvc7826RWQpBxObz0oQAxadnVCwgJ43XEGhJJrrcs/mli',1);
INSERT INTO `user` VALUES (3,'user3','$2a$12$nbrlhOxErstHKvSJyjzJ1uCAhItuoukWXlyYXWiTqf/Z/QSNPNP8O',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `user_detail` WRITE;
/*!40000 ALTER TABLE `user_detail` DISABLE KEYS */;
INSERT INTO `user_detail` VALUES (1,'UserName','UserSurname','UserEmail@email.com','APP');
INSERT INTO `user_detail` VALUES (2,'UserName','UserSurname','UserEmail@email.com','APP');
INSERT INTO `user_detail` VALUES (3,'UserName','UserSurname','UserEmail@email.com','APP');
/*!40000 ALTER TABLE `user_detail` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES (1,'USER');
INSERT INTO `authority` VALUES (2,'ADMIN');
INSERT INTO `authority` VALUES (3,'USER');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1,'Sektret sukcesu KFC',1,'<h1>KFC (Kentucky Fried Chicken) </h1><p>Jest to amerykańska sieć restauracji fast food, która specjalizuje się w przysmakach z kurczaka. Jest to jedna z największych sieci fast food na świecie, z ponad 24 000 restauracji w ponad 145 krajach.</p><p>Historia KFC sięga lat 30. XX wieku, kiedy to Harland Sanders, zwany \"Colonel Sanders\", zaczął sprzedawać swoje chrupiące kawałki kurczaka w swojej restauracji w Corbin, w stanie Kentucky. Jego pomysł na smażonego kurczaka w ciekawym panierce odniósł sukces, a Sanders zaczął udzielać franczyzy innym restauracjom, co przyczyniło się do rozwoju sieci KFC.</p><p><img src=\"https://pbs.twimg.com/media/E1P8m_3XsAIoyht.jpg\" alt=\"Janusz Korwin-Mikke on Twitter: &quot;Pilne‼️Jonasz Koran-Mekka?? i Ozjasz  Goldberg?? są głęboko zaniepokojeni ostatnimi wydarzeniami w  Izraelu/Palestynie. Dlatego zapraszają na dzisiejszy live chat o 21.30 na  https://t.co/NIh6wrvnw7 #Korwin #Izrael ...\"></p><p><br></p><h2>Menu</h2><p>Obecnie, KFC oferuje szeroki wybór dań z kurczaka, takich jak chrupiące kawałki, skrzydełka, filety, zrazy i wiele innych. Oprócz tego, w menu można znaleźć frytki, sałatki, napoje i desery.</p><p>KFC stawia na jakość swojego jedzenia, dlatego wszystkie kawałki kurczaka są przygotowywane na bieżąco i smażone w oleju roślinnym, który nie zawiera szkodliwych substancji. Restauracje KFC oferują także opcje dla wegetarian, w tym sałatki i burger z wegetariańskim kotlecikiem.</p><p><br></p><h2>Kampanie reklamowe</h2><p>KFC jest znane nie tylko z pysznego jedzenia, ale także z innowacyjnych kampanii reklamowych. W 2019 roku, KFC wprowadziło na rynek wegańskiego kurczaka, co wywołało spore zainteresowanie wśród konsumentów. Restauracja wprowadziła również program \"KFC Colonel\'s Scholars\", który zapewnia stypendia dla studentów na studiach licencjackich w USA.</p><p><br></p><h2>Kontrowersje</h2><p>Jednakże, KFC nie jest pozbawione kontrowersji. W niektórych krajach, restauracje KFC są krytykowane za swoją szkodliwość dla zdrowia ze względu na dużą ilość tłuszczu i kalorii w oferowanych posiłkach. W 2003 roku, KFC zostało również oskarżone o wykorzystywanie pracowników zmuszanych do pracy w niehumanitarnych warunkach.</p><p>Mimo to, KFC pozostaje popularną siecią restauracji fast food na całym świecie. Z pewnością, smak chrupiącego kurczaka pozostanie ulubionym daniem wielu osób na długo.</p>','politics','2023-03-20 00:00:00'),(2,'Minecraft 101',1,'<h1 class=\'ql-align-justify\'>Minecraft: A Beginner\'s Guide to Survival and Exploration</h1><p class=\'ql-align-justify\'>Minecraft is a game that has captured the hearts and minds of millions of players around the world. It\'s a game that can be enjoyed by people of all ages, from young children to adults. The game is incredibly popular due to its open-ended gameplay, which allows players to explore, create, and survive in a world made entirely of blocks.</p><p class=\'ql-align-justify\'>In this article, we\'ll take a look at the basics of Minecraft, including how to survive, explore, and create. Whether you\'re a seasoned Minecraft veteran or a complete beginner, this guide will help you get started on your Minecraft journey.</p><p class=\'ql-align-justify\'><img src=\'https://gaming.komputronik.pl/wp-content/uploads/2022/11/jak-zrobic-przedmiot-w-minecraft-768x432.jpg\' alt=\'Minecraft: craftowanie - jak zrobi rzeczy w grze?\'></p><p class=\'ql-align-justify\'><br></p><h2 class=\'ql-align-justify\'>Survival</h2><p class=\'ql-align-justify\'>Survival is the core of Minecraft. When you first start the game, you\'re dropped into a randomly generated world with nothing but your bare hands. You\'ll need to gather resources, build shelter, and fend off hostile creatures in order to survive.</p><p class=\'ql-align-justify\'>The first thing you should do when you start a new game is to gather some wood. Wood is a versatile resource that can be used to make tools, weapons, and shelter. To gather wood, find a tree and punch it with your bare hands. This will cause the tree to drop wood blocks that you can pick up.</p><p class=\'ql-align-justify\'>Once you have some wood, you can use it to make a crafting table. A crafting table is a block that allows you to craft more complex items. To make a crafting table, open your inventory and arrange four wood blocks in a square pattern. Once you\'ve crafted a crafting table, place it down in the world by right-clicking.</p><p class=\'ql-align-justify\'>Now that you have a crafting table, you can use it to make more advanced tools and weapons. You can also use it to craft a shelter to protect yourself from hostile creatures.</p><p class=\'ql-align-justify\'><br></p><p class=\'ql-align-justify\'><img src=\'https://samequizy.pl/wp-content/uploads/2019/02/filing_images_d9125fbf157a.jpg\' alt=\'MineCraft Survival | sameQuizy\'></p><p class=\'ql-align-justify\'><br></p><h2 class=\'ql-align-justify\'>Exploration</h2><p class=\'ql-align-justify\'>Exploration is another key part of Minecraft. The game world is vast and full of interesting places to discover. There are caves to explore, mountains to climb, and oceans to cross.</p><p class=\'ql-align-justify\'>To start exploring, you\'ll need to gather some basic supplies. You\'ll want to bring food, tools, and weapons with you. Food can be gathered by killing animals or by harvesting crops. Tools and weapons can be crafted using resources you find in the world.</p><p class=\'ql-align-justify\'>As you explore the world, you\'ll encounter various biomes. Biomes are regions of the world that have a unique climate and landscape. Some biomes are snowy, while others are desert or jungle. Each biome has its own set of resources and creatures to discover.</p><p class=\'ql-align-justify\'>One of the most exciting places to explore in Minecraft is the Nether. The Nether is a dangerous, fiery realm that can be accessed by building a special portal. The Nether is home to unique resources and creatures, as well as dangerous lava lakes and towering fortresses.</p><h2 class=\'ql-align-justify\'><br></h2><h2 class=\'ql-align-justify\'>Creation</h2><p class=\'ql-align-justify\'>One of the things that sets Minecraft apart from other games is its creative mode. Creative mode allows you to build anything you can imagine without the need for resources or survival concerns.</p><p class=\'ql-align-justify\'>In creative mode, you have access to all of the resources in the game. You can fly, place blocks instantly, and destroy them just as quickly. You can build massive structures, intricate redstone contraptions, or anything else you can dream up.</p><p class=\'ql-align-justify\'>Creative mode is a great way to experiment with different building techniques and to showcase your creativity. You can share your creations with other players online, or you can just enjoy them on your own.</p><h2 class=\'ql-align-justify\'><br></h2><h2 class=\'ql-align-justify\'>Conclusion</h2><p class=\'ql-align-justify\'>Minecraft is a game that offers endless possibilities for exploration, survival, and creation. Whether you\'re looking to build massive structures, explore vast landscapes, or survive against hordes of monsters, Minecraft has something for everyone.</p><p class=\'ql-align-justify\'>In this guide, we\'ve covered the basics of survival, exploration, and creation in Minecraft.</p><p><br></p>','nature','2023-03-20 00:00:00'),(3,'Sherlock Holmes znowu w akcji',1,'<h1>Robert Makłowicz </h1><p>to jedna z najbardziej rozpoznawalnych postaci w polskim świecie kulinarnym. Jego pasja do gotowania i odkrywania smaków różnych krajów przyciąga uwagę widzów od ponad dwóch dekad. Poniżej przedstawiamy artykuł o Robertcie Makłowiczu podzielony na kilka sekcji.</p><p><br></p><p><img src=\"https://kulinarnapolska.org/wp-content/uploads/2017/08/robert-mak%C5%82owicz-tvp-tvn.jpg\" alt=\"Makłowicz w Drodze — Kulinarna Polska | Przepisy, gotowanie, jedzenie\"></p><p><br></p><h2>Kim jest Robert Makłowicz?</h2><p>Robert Makłowicz to znany kucharz, dziennikarz i podróżnik. Urodził się w 1963 roku w Bydgoszczy i od najmłodszych lat interesował się gotowaniem. W 1987 roku ukończył studia na Wydziale Nauk Spożywczych Uniwersytetu Warmińsko-Mazurskiego w Olsztynie. Przez wiele lat pracował jako nauczyciel gastronomii i kucharz w restauracjach w Polsce i za granicą.</p><p><br></p><h2>Telewizyjna kariera Roberta Makłowicza</h2><p>Robert Makłowicz zaczął swoją karierę telewizyjną w 1997 roku, kiedy to prowadził program \"Kuchnia polska\". Później zyskał jeszcze większą popularność dzięki programowi \"Podróże kulinarne\", w którym opowiadał o kuchni różnych krajów i kultur. W 2012 roku Robert Makłowicz został uhonorowany Nagrodą Kryształowej Kaczki dla Najpopularniejszego Osobowości Telewizyjnej.</p><p><br></p><h2>Książki Roberta Makłowicza</h2><p>Robert Makłowicz jest autorem wielu książek kulinarnych, w których dzieli się swoimi przepisami i doświadczeniem. Wśród jego najpopularniejszych publikacji znajdują się \"Podróże kulinarne Roberta Makłowicza\", \"Kuchnia polska\" oraz \"Wszystko o winie\".</p><h2><br></h2><h2>Pasja Roberta Makłowicza</h2><p>Robert Makłowicz słynie z ogromnej pasji do gotowania i odkrywania nowych smaków. Podczas swoich podróży stara się poznać kuchnię różnych krajów i regionów, a potem przekazywać swoją wiedzę i doświadczenie innym. Jego programy telewizyjne i książki to doskonałe źródło inspiracji dla osób, które chcą rozwijać swoje umiejętności kulinarne i eksperymentować w kuchni.</p><h2><br></h2><h2>Robert Makłowicz a promocja polskiej kuchni</h2><p>Robert Makłowicz w swojej pracy bardzo często promuje polską kuchnię i produkty regionalne. Zawsze stara się podkreślać, że kuchnia polska jest bogata w smaki i aromaty, a jej potrawy są warte poznania i spróbowania. Dzięki temu przyczynił się do z</p>','politics','2023-03-20 13:23:44'),(5,'Inwestycje lokalne',1,'<h1>Inwestycje lokalne</h1><p>Zyskuj one coraz wiksz popularno wrd inwestorw. Polega on na inwestowaniu w projekty i przedsiwzicia, ktre maj na celu rozwj lokalnej spoecznoci i przynoszenie korzyci dla jej mieszkacw.</p><p>Inwestycje lokalne maj wiele zalet, zarwno dla inwestorw, jak i dla mieszkacw danego regionu. Dziki nim moliwe jest rozwijanie lokalnej infrastruktury, poprawa jakoci ycia mieszkacw oraz zwikszenie atrakcyjnoci danego regionu dla turystw i przedsibiorcw. Inwestycje lokalne s rwnie sposobem na wspieranie rozwoju lokalnej gospodarki, co moe przynie korzyci dla caej spoecznoci.</p><p><br></p><p>Przykadem inwestycji lokalnej moe by np. budowa parku, placu zabaw, czy cieki rowerowej. Tego typu przedsiwzicia przynosz korzyci dla mieszkacw, a take mog przyciga turystw i inwestorw, ktrzy zechc lokowa swoje pienidze w tej okolicy. Inwestycje lokalne mog by rwnie zwizane z rozwojem lokalnego biznesu, np. poprzez inwestycje w rozwj sektora turystycznego lub lokalnych producentw ywnoci.</p><p><img src=\"https://compote.slate.com/images/926e5009-c10a-48fe-b90e-fa0760f82fcd.png?crop=680%2C453%2Cx0%2Cy0\" alt=\"Stonks: Why we\'re all saying it now.\"></p><p>Inwestycje lokalne nie wymagaj wielkich nakadw finansowych, a ich efekty mog by widoczne ju po krtkim czasie. Dlatego s one szczeglnie atrakcyjne dla mniejszych inwestorw, ktrzy chc zainwestowa swoje pienidze w sposb bezporednio zwizany z miejscem, w ktrym yj.</p><p><br></p><p>Warto jednak pamita, e inwestycje lokalne wi si rwnie z pewnymi ryzykami, takimi jak brak pewnoci co do przyszych zyskw czy zmiany w warunkach rynkowych. Dlatego przed podjciem decyzji o inwestycji lokalnej warto dokadnie przemyle jej ryzyka i potencjalne korzyci.</p><p><br></p><p>Podsumowujc, inwestycje lokalne to sposb na inwestowanie pienidzy w sposb zwizany z miejscem, w ktrym yjemy, co przynosi korzyci dla lokalnej spoecznoci i moe przynie zyski dla inwestorw. Jednoczenie warto pamita o ryzykach zwizanych z tego typu inwestycjami i dokadnie przemyle swoj decyzj przed podjciem inwestycji.</p>','nature','2023-03-20 00:00:00'),(6,'Nowy Shrek już wkrótce!',1,'<h1>Najnowsze premiery kinowe  </h1><p>\"Powracają nasze ulubione postacie z bajki! Tym razem Shrek musi stawić czoła jeszcze większym wyzwaniom niż poprzednio. Na drodze do osiągnięcia spokoju i normalnego życia stanie mu nowy wróg, który chce zniszczyć wszystko, co Shrek i jego przyjaciele zbudowali. Czy Shrek i jego drużyna będą w stanie pokonać tę nową, groźną siłę? Tylko czas pokaże, czy nasz bohater zdoła uratować swoich najbliższych i odzyskać spokój w swoim świecie. Przygotujcie się na kolejną porcję przygód, śmiechu i emocji - Shrek is back!\"</p><p><img src=\"https://cdn.vox-cdn.com/thumbor/VZNJM5S4Cw2i3JaycT9waVLCwqw=/715x248:1689x721/1200x800/filters:focal(972x299:1278x605)/cdn.vox-cdn.com/uploads/chorus_image/image/69305239/shrek4_disneyscreencaps.com_675.0.jpg\" alt=\"Shrek and Pepe the Frog are similar kinds of meme stars - Polygon\"></p><p><br></p><h2>Kim jest Robert Makłowicz?</h2><p>Robert Makłowicz to znany kucharz, dziennikarz i podróżnik. Urodził się w 1963 roku w Bydgoszczy i od najmłodszych lat interesował się gotowaniem. W 1987 roku ukończył studia na Wydziale Nauk Spożywczych Uniwersytetu Warmińsko-Mazurskiego w Olsztynie. Przez wiele lat pracował jako nauczyciel gastronomii i kucharz w restauracjach w Polsce i za granicą.</p><p><br></p><h2>Telewizyjna kariera Roberta Makłowicza</h2><p>Robert Makłowicz zaczął swoją karierę telewizyjną w 1997 roku, kiedy to prowadził program \"Kuchnia polska\". Później zyskał jeszcze większą popularność dzięki programowi \"Podróże kulinarne\", w którym opowiadał o kuchni różnych krajów i kultur. W 2012 roku Robert Makłowicz został uhonorowany Nagrodą Kryształowej Kaczki dla Najpopularniejszego Osobowości Telewizyjnej.</p><p><br></p><h2>Książki Roberta Makłowicza</h2><p>Robert Makłowicz jest autorem wielu książek kulinarnych, w których dzieli się swoimi przepisami i doświadczeniem. Wśród jego najpopularniejszych publikacji znajdują się \"Podróże kulinarne Roberta Makłowicza\", \"Kuchnia polska\" oraz \"Wszystko o winie\".</p><h2><br></h2><h2>Pasja Roberta Makłowicza</h2><p>Robert Makłowicz słynie z ogromnej pasji do gotowania i odkrywania nowych smaków. Podczas swoich podróży stara się poznać kuchnię różnych krajów i regionów, a potem przekazywać swoją wiedzę i doświadczenie innym. Jego programy telewizyjne i książki to doskonałe źródło inspiracji dla osób, które chcą rozwijać swoje umiejętności kulinarne i eksperymentować w kuchni.</p><h2><br></h2><h2>Robert Makłowicz a promocja polskiej kuchni</h2><p>Robert Makłowicz w swojej pracy bardzo często promuje polską kuchnię i produkty regionalne. Zawsze stara się podkreślać, że kuchnia polska jest bogata w smaki i aromaty, a jej potrawy są warte poznania i spróbowania. Dzięki temu przyczynił się do z</p>','politics','2023-03-20 13:23:44'),(8,'Kolejny rekord pobity!',1,'<h1>Najnowsze premiery kinowe  </h1><p>\"Powracają nasze ulubione postacie z bajki! Tym razem Shrek musi stawić czoła jeszcze większym wyzwaniom niż poprzednio. Na drodze do osiągnięcia spokoju i normalnego życia stanie mu nowy wróg, który chce zniszczyć wszystko, co Shrek i jego przyjaciele zbudowali. Czy Shrek i jego drużyna będą w stanie pokonać tę nową, groźną siłę? Tylko czas pokaże, czy nasz bohater zdoła uratować swoich najbliższych i odzyskać spokój w swoim świecie. Przygotujcie się na kolejną porcję przygód, śmiechu i emocji - Shrek is back!\"</p><p><img src=\"https://pliki.meczyki.pl/big700/390/61bde4a7ac7bc.JPG\" alt=\"Klasyfikacja najlepszych lotników w polskich skokach. Adam Małysz daleko  poza podium\"></p><p><br></p><h2>Kim jest Robert Makłowicz?</h2><p>Robert Makłowicz to znany kucharz, dziennikarz i podróżnik. Urodził się w 1963 roku w Bydgoszczy i od najmłodszych lat interesował się gotowaniem. W 1987 roku ukończył studia na Wydziale Nauk Spożywczych Uniwersytetu Warmińsko-Mazurskiego w Olsztynie. Przez wiele lat pracował jako nauczyciel gastronomii i kucharz w restauracjach w Polsce i za granicą.</p><p><br></p><h2>Telewizyjna kariera Roberta Makłowicza</h2><p>Robert Makłowicz zaczął swoją karierę telewizyjną w 1997 roku, kiedy to prowadził program \"Kuchnia polska\". Później zyskał jeszcze większą popularność dzięki programowi \"Podróże kulinarne\", w którym opowiadał o kuchni różnych krajów i kultur. W 2012 roku Robert Makłowicz został uhonorowany Nagrodą Kryształowej Kaczki dla Najpopularniejszego Osobowości Telewizyjnej.</p><p><br></p><h2>Książki Roberta Makłowicza</h2><p>Robert Makłowicz jest autorem wielu książek kulinarnych, w których dzieli się swoimi przepisami i doświadczeniem. Wśród jego najpopularniejszych publikacji znajdują się \"Podróże kulinarne Roberta Makłowicza\", \"Kuchnia polska\" oraz \"Wszystko o winie\".</p><h2><br></h2><h2>Pasja Roberta Makłowicza</h2><p>Robert Makłowicz słynie z ogromnej pasji do gotowania i odkrywania nowych smaków. Podczas swoich podróży stara się poznać kuchnię różnych krajów i regionów, a potem przekazywać swoją wiedzę i doświadczenie innym. Jego programy telewizyjne i książki to doskonałe źródło inspiracji dla osób, które chcą rozwijać swoje umiejętności kulinarne i eksperymentować w kuchni.</p><h2><br></h2><h2>Robert Makłowicz a promocja polskiej kuchni</h2><p>Robert Makłowicz w swojej pracy bardzo często promuje polską kuchnię i produkty regionalne. Zawsze stara się podkreślać, że kuchnia polska jest bogata w smaki i aromaty, a jej potrawy są warte poznania i spróbowania. Dzięki temu przyczynił się do z</p>','nature','2023-03-20 00:00:00'),(9,'Największy międzynarodowy crusher',1,'<h1 class=\"ql-align-justify\">Minecraft: A Beginner\'s Guide to Survival and Exploration</h1><p class=\"ql-align-justify\">Minecraft is a game that has captured the hearts and minds of millions of players around the world. It\'s a game that can be enjoyed by people of all ages, from young children to adults. The game is incredibly popular due to its open-ended gameplay, which allows players to explore, create, and survive in a world made entirely of blocks.</p><p class=\"ql-align-justify\">In this article, we\'ll take a look at the basics of Minecraft, including how to survive, explore, and create. Whether you\'re a seasoned Minecraft veteran or a complete beginner, this guide will help you get started on your Minecraft journey.</p><p class=\"ql-align-justify\"><img src=\"https://static.android.com.pl/uploads/2023/03/Pedro-Pascal.jpg\" alt=\"Pedro Pascal to fenomen. Poza The Last of Us i Grą o Tron ma na koncie  także inne świetne role\"></p><p class=\"ql-align-justify\"><br></p><h2 class=\"ql-align-justify\">Survival</h2><p class=\"ql-align-justify\">Survival is the core of Minecraft. When you first start the game, you\'re dropped into a randomly generated world with nothing but your bare hands. You\'ll need to gather resources, build shelter, and fend off hostile creatures in order to survive.</p><p class=\"ql-align-justify\">The first thing you should do when you start a new game is to gather some wood. Wood is a versatile resource that can be used to make tools, weapons, and shelter. To gather wood, find a tree and punch it with your bare hands. This will cause the tree to drop wood blocks that you can pick up.</p><p class=\"ql-align-justify\">Once you have some wood, you can use it to make a crafting table. A crafting table is a block that allows you to craft more complex items. To make a crafting table, open your inventory and arrange four wood blocks in a square pattern. Once you\'ve crafted a crafting table, place it down in the world by right-clicking.</p><p class=\"ql-align-justify\">Now that you have a crafting table, you can use it to make more advanced tools and weapons. You can also use it to craft a shelter to protect yourself from hostile creatures.</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p><h2 class=\"ql-align-justify\">Exploration</h2><p class=\"ql-align-justify\">Exploration is another key part of Minecraft. The game world is vast and full of interesting places to discover. There are caves to explore, mountains to climb, and oceans to cross.</p><p class=\"ql-align-justify\">To start exploring, you\'ll need to gather some basic supplies. You\'ll want to bring food, tools, and weapons with you. Food can be gathered by killing animals or by harvesting crops. Tools and weapons can be crafted using resources you find in the world.</p><p class=\"ql-align-justify\">As you explore the world, you\'ll encounter various biomes. Biomes are regions of the world that have a unique climate and landscape. Some biomes are snowy, while others are desert or jungle. Each biome has its own set of resources and creatures to discover.</p><p class=\"ql-align-justify\">One of the most exciting places to explore in Minecraft is the Nether. The Nether is a dangerous, fiery realm that can be accessed by building a special portal. The Nether is home to unique resources and creatures, as well as dangerous lava lakes and towering fortresses.</p><h2 class=\"ql-align-justify\"><br></h2><h2 class=\"ql-align-justify\">Creation</h2><p class=\"ql-align-justify\">One of the things that sets Minecraft apart from other games is its creative mode. Creative mode allows you to build anything you can imagine without the need for resources or survival concerns.</p><p class=\"ql-align-justify\">In creative mode, you have access to all of the resources in the game. You can fly, place blocks instantly, and destroy them just as quickly. You can build massive structures, intricate redstone contraptions, or anything else you can dream up.</p><p class=\"ql-align-justify\">Creative mode is a great way to experiment with different building techniques and to showcase your creativity. You can share your creations with other players online, or you can just enjoy them on your own.</p><h2 class=\"ql-align-justify\"><br></h2><h2 class=\"ql-align-justify\">Conclusion</h2><p class=\"ql-align-justify\">Minecraft is a game that offers endless possibilities for exploration, survival, and creation. Whether you\'re looking to build massive structures, explore vast landscapes, or survive against hordes of monsters, Minecraft has something for everyone.</p><p class=\"ql-align-justify\">In this guide, we\'ve covered the basics of survival, exploration, and creation in Minecraft.</p><p><br></p>','politics','2023-03-20 13:23:44'),(11,'Nowy trendsetter',1,'<h1 class=\"ql-align-justify\">Minecraft: A Beginner\'s Guide to Survival and Exploration</h1><p class=\"ql-align-justify\">Minecraft is a game that has captured the hearts and minds of millions of players around the world. It\'s a game that can be enjoyed by people of all ages, from young children to adults. The game is incredibly popular due to its open-ended gameplay, which allows players to explore, create, and survive in a world made entirely of blocks.</p><p class=\"ql-align-justify\">In this article, we\'ll take a look at the basics of Minecraft, including how to survive, explore, and create. Whether you\'re a seasoned Minecraft veteran or a complete beginner, this guide will help you get started on your Minecraft journey.</p><p class=\"ql-align-justify\"><img src=\"https://akm-img-a-in.tosshub.com/indiatoday/images/story/202303/befunky-collage_-_2023-03-28t140707.297-sixteen_nine.jpg?VersionId=mtpyw_BznKSUoWgbJ0XAZ3A1yEilesVX\" alt=\"AI-generated pic of Pope Francis in bougie puffer jacket goes viral - India  Today\"></p><p class=\"ql-align-justify\"><br></p><h2 class=\"ql-align-justify\">Survival</h2><p class=\"ql-align-justify\">Survival is the core of Minecraft. When you first start the game, you\'re dropped into a randomly generated world with nothing but your bare hands. You\'ll need to gather resources, build shelter, and fend off hostile creatures in order to survive.</p><p class=\"ql-align-justify\">The first thing you should do when you start a new game is to gather some wood. Wood is a versatile resource that can be used to make tools, weapons, and shelter. To gather wood, find a tree and punch it with your bare hands. This will cause the tree to drop wood blocks that you can pick up.</p><p class=\"ql-align-justify\">Once you have some wood, you can use it to make a crafting table. A crafting table is a block that allows you to craft more complex items. To make a crafting table, open your inventory and arrange four wood blocks in a square pattern. Once you\'ve crafted a crafting table, place it down in the world by right-clicking.</p><p class=\"ql-align-justify\">Now that you have a crafting table, you can use it to make more advanced tools and weapons. You can also use it to craft a shelter to protect yourself from hostile creatures.</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p><h2 class=\"ql-align-justify\">Exploration</h2><p class=\"ql-align-justify\">Exploration is another key part of Minecraft. The game world is vast and full of interesting places to discover. There are caves to explore, mountains to climb, and oceans to cross.</p><p class=\"ql-align-justify\">To start exploring, you\'ll need to gather some basic supplies. You\'ll want to bring food, tools, and weapons with you. Food can be gathered by killing animals or by harvesting crops. Tools and weapons can be crafted using resources you find in the world.</p><p class=\"ql-align-justify\">As you explore the world, you\'ll encounter various biomes. Biomes are regions of the world that have a unique climate and landscape. Some biomes are snowy, while others are desert or jungle. Each biome has its own set of resources and creatures to discover.</p><p class=\"ql-align-justify\">One of the most exciting places to explore in Minecraft is the Nether. The Nether is a dangerous, fiery realm that can be accessed by building a special portal. The Nether is home to unique resources and creatures, as well as dangerous lava lakes and towering fortresses.</p><h2 class=\"ql-align-justify\"><br></h2><h2 class=\"ql-align-justify\">Creation</h2><p class=\"ql-align-justify\">One of the things that sets Minecraft apart from other games is its creative mode. Creative mode allows you to build anything you can imagine without the need for resources or survival concerns.</p><p class=\"ql-align-justify\">In creative mode, you have access to all of the resources in the game. You can fly, place blocks instantly, and destroy them just as quickly. You can build massive structures, intricate redstone contraptions, or anything else you can dream up.</p><p class=\"ql-align-justify\">Creative mode is a great way to experiment with different building techniques and to showcase your creativity. You can share your creations with other players online, or you can just enjoy them on your own.</p><h2 class=\"ql-align-justify\"><br></h2><h2 class=\"ql-align-justify\">Conclusion</h2><p class=\"ql-align-justify\">Minecraft is a game that offers endless possibilities for exploration, survival, and creation. Whether you\'re looking to build massive structures, explore vast landscapes, or survive against hordes of monsters, Minecraft has something for everyone.</p><p class=\"ql-align-justify\">In this guide, we\'ve covered the basics of survival, exploration, and creation in Minecraft.</p><p><br></p>','nature','2023-03-20 00:00:00'),(12,'Ekranizacja Mario',1,'<h1 class=\"ql-align-justify\">Minecraft: A Beginner\'s Guide to Survival and Exploration</h1><p class=\"ql-align-justify\">Minecraft is a game that has captured the hearts and minds of millions of players around the world. It\'s a game that can be enjoyed by people of all ages, from young children to adults. The game is incredibly popular due to its open-ended gameplay, which allows players to explore, create, and survive in a world made entirely of blocks.</p><p class=\"ql-align-justify\">In this article, we\'ll take a look at the basics of Minecraft, including how to survive, explore, and create. Whether you\'re a seasoned Minecraft veteran or a complete beginner, this guide will help you get started on your Minecraft journey.</p><p class=\"ql-align-justify\"><img src=\"https://images.nintendolife.com/fea3b5705cd9f/1280x720.jpg\" alt=\"Random: Jack Black Wears \'Revealing\' Bowser Costume For Mario Movie Promo |  Nintendo Life\"></p><p class=\"ql-align-justify\"><br></p><h2 class=\"ql-align-justify\">Survival</h2><p class=\"ql-align-justify\">Survival is the core of Minecraft. When you first start the game, you\'re dropped into a randomly generated world with nothing but your bare hands. You\'ll need to gather resources, build shelter, and fend off hostile creatures in order to survive.</p><p class=\"ql-align-justify\">The first thing you should do when you start a new game is to gather some wood. Wood is a versatile resource that can be used to make tools, weapons, and shelter. To gather wood, find a tree and punch it with your bare hands. This will cause the tree to drop wood blocks that you can pick up.</p><p class=\"ql-align-justify\">Once you have some wood, you can use it to make a crafting table. A crafting table is a block that allows you to craft more complex items. To make a crafting table, open your inventory and arrange four wood blocks in a square pattern. Once you\'ve crafted a crafting table, place it down in the world by right-clicking.</p><p class=\"ql-align-justify\">Now that you have a crafting table, you can use it to make more advanced tools and weapons. You can also use it to craft a shelter to protect yourself from hostile creatures.</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p><h2 class=\"ql-align-justify\">Exploration</h2><p class=\"ql-align-justify\">Exploration is another key part of Minecraft. The game world is vast and full of interesting places to discover. There are caves to explore, mountains to climb, and oceans to cross.</p><p class=\"ql-align-justify\">To start exploring, you\'ll need to gather some basic supplies. You\'ll want to bring food, tools, and weapons with you. Food can be gathered by killing animals or by harvesting crops. Tools and weapons can be crafted using resources you find in the world.</p><p class=\"ql-align-justify\">As you explore the world, you\'ll encounter various biomes. Biomes are regions of the world that have a unique climate and landscape. Some biomes are snowy, while others are desert or jungle. Each biome has its own set of resources and creatures to discover.</p><p class=\"ql-align-justify\">One of the most exciting places to explore in Minecraft is the Nether. The Nether is a dangerous, fiery realm that can be accessed by building a special portal. The Nether is home to unique resources and creatures, as well as dangerous lava lakes and towering fortresses.</p><h2 class=\"ql-align-justify\"><br></h2><h2 class=\"ql-align-justify\">Creation</h2><p class=\"ql-align-justify\">One of the things that sets Minecraft apart from other games is its creative mode. Creative mode allows you to build anything you can imagine without the need for resources or survival concerns.</p><p class=\"ql-align-justify\">In creative mode, you have access to all of the resources in the game. You can fly, place blocks instantly, and destroy them just as quickly. You can build massive structures, intricate redstone contraptions, or anything else you can dream up.</p><p class=\"ql-align-justify\">Creative mode is a great way to experiment with different building techniques and to showcase your creativity. You can share your creations with other players online, or you can just enjoy them on your own.</p><h2 class=\"ql-align-justify\"><br></h2><h2 class=\"ql-align-justify\">Conclusion</h2><p class=\"ql-align-justify\">Minecraft is a game that offers endless possibilities for exploration, survival, and creation. Whether you\'re looking to build massive structures, explore vast landscapes, or survive against hordes of monsters, Minecraft has something for everyone.</p><p class=\"ql-align-justify\">In this guide, we\'ve covered the basics of survival, exploration, and creation in Minecraft.</p><p><br></p>','politics','2023-03-20 13:23:44');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `endorsement` WRITE;
/*!40000 ALTER TABLE `endorsement` DISABLE KEYS */;
INSERT INTO `endorsement` VALUES (1,1,1),(2,1,0);
/*!40000 ALTER TABLE `endorsement` ENABLE KEYS */;
UNLOCK TABLES;
