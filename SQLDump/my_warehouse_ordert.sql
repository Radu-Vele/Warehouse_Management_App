-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: my_warehouse
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ordert`
--

DROP TABLE IF EXISTS `ordert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordert` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `clientEmail` varchar(45) DEFAULT NULL,
  `productID` varchar(45) DEFAULT NULL,
  `numberOfItems` varchar(45) DEFAULT NULL,
  `processingDate` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordert`
--

LOCK TABLES `ordert` WRITE;
/*!40000 ALTER TABLE `ordert` DISABLE KEYS */;
INSERT INTO `ordert` VALUES (1,'ggSport@obs.de','4','10','2022-04-16 15:23:35.38'),(2,'ion@gmail.br','4','10','2022-04-16 17:47:37.685'),(3,'alin@rapidbuc.kaz','9','2','2022-04-16 17:57:00.051'),(4,'ggSport@obs.de','9','5','2022-04-16 17:57:13.414'),(5,'alin@rapidbuc.kaz','4','10','2022-04-17 12:49:39.935'),(6,'andreiboss@steaua.de','4','10','2022-04-17 12:50:17.198'),(7,'andreiboss@steaua.de','4','1','2022-04-17 12:51:02.638'),(8,'alin@rapidbuc.kaz','4','1','2022-04-17 12:52:55.729'),(9,'ggSport@obs.de','4','1','2022-04-17 12:54:17.496'),(10,'alin@rapidbuc.kaz','4','10','2022-04-17 12:55:18.693'),(11,'alin@rapidbuc.kaz','4','1','2022-04-17 12:56:10.954'),(12,'andreiboss@steaua.de','4','1','2022-04-17 12:56:47.342'),(13,'alin@rapidbuc.kaz','4','10','2022-04-17 12:58:20.654'),(14,'andreiboss@steaua.de','4','1','2022-04-17 12:59:26.662'),(15,'andreiboss@steaua.de','4','10','2022-04-17 13:01:49.892'),(16,'ion@gmail.br','8','1','2022-04-17 13:06:18.326'),(17,'alin@rapidbuc.kaz','4','2','2022-04-17 13:08:40.334'),(18,'alin@rapidbuc.kaz','4','2','2022-04-17 13:08:54.046'),(19,'ggSport@obs.de','4','10','2022-04-17 13:09:55.231'),(20,'andreiboss@steaua.de','4','10','2022-04-17 13:10:59.751'),(21,'andreiboss@steaua.de','4','3','2022-04-17 13:15:08.442'),(22,'alin@rapidbuc.kaz','4','10','2022-04-17 13:18:31.317'),(23,'alin@rapidbuc.kaz','8','1','2022-04-17 13:20:17.701'),(24,'ion@gmail.br','9','1','2022-04-17 13:21:17.793'),(25,'ggSport@obs.de','11','20','2022-04-19 13:14:42.93'),(26,'alin@rapidbuc.kaz','11','2','2022-04-19 21:13:05.535'),(27,'ggSport@obs.ro','12','300','2022-04-19 21:27:59.203'),(28,'ggSport@obs.ro','12','1','2022-04-19 22:33:15.96'),(29,'ggSport@obs.ro','9','1','2022-04-19 22:45:06.956'),(30,'ben@gey.us','9','1','2022-04-19 23:01:12.607'),(31,'ggSport@obs.io','12','2','2022-04-19 23:30:15.58'),(32,'ggSport@obs.io','12','10','2022-04-21 01:42:15.802');
/*!40000 ALTER TABLE `ordert` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-21  2:31:13
