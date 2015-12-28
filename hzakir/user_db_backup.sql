-- MySQL dump 10.13  Distrib 5.1.60, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: user_db
-- ------------------------------------------------------
-- Server version	5.1.60

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` text,
  `distance` int(11) NOT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'huzefazakir',0,'2012-07-09 00:47:36'),(2,'user_1',3001629,'2012-07-01 23:42:03'),(3,'user_2',2477155,'2012-07-01 23:42:16'),(4,'user_3',1239144,'2012-07-01 23:42:29'),(5,'user_4',8645763,'2012-07-01 23:42:44'),(6,'user_5',2696017,'2012-07-01 23:42:57');
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_location_table`
--

DROP TABLE IF EXISTS `user_location_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_location_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) DEFAULT NULL,
  `logitude` float DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `cur_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_location_table`
--

LOCK TABLES `user_location_table` WRITE;
/*!40000 ALTER TABLE `user_location_table` DISABLE KEYS */;
INSERT INTO `user_location_table` VALUES (1,'huzefazakir',67,21,'2012-07-09 00:47:36'),(2,'invisible',52,45,'2012-07-21 22:07:09'),(3,'invisible',44,29,'2012-07-21 22:07:13'),(4,'user_3',65,32,'2012-07-01 23:42:29'),(5,'user_4',0,-21,'2012-07-01 23:42:44'),(6,'user_5',-87,-201,'2012-07-01 23:42:57');
/*!40000 ALTER TABLE `user_location_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_location_table_v1`
--

DROP TABLE IF EXISTS `user_location_table_v1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_location_table_v1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `cur_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fb_id` bigint(20) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=206 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_location_table_v1`
--

LOCK TABLES `user_location_table_v1` WRITE;
/*!40000 ALTER TABLE `user_location_table_v1` DISABLE KEYS */;
INSERT INTO `user_location_table_v1` VALUES (203,'Huzefa Zakir',37.422,-122.084,'2013-06-29 06:27:37',100001942506831,'Mountain View, California'),(204,'Nafisa Zakir',37.422,-122.084,'2013-06-24 07:25:54',764420316,'Mountain View, California'),(205,'Moiz Bootwalla',37.422,-122.084,'2013-06-29 05:26:22',684135034,'Mountain View, California');
/*!40000 ALTER TABLE `user_location_table_v1` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-06-28 23:53:06
