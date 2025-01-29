-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: quick_park_assist
-- ------------------------------------------------------
-- Server version	8.1.0

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
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `full_name` varchar(255) NOT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `parking_spot` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `booking_id` bigint NOT NULL AUTO_INCREMENT,
  `spot_id` int DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `status` enum('CONFIRMED','PENDING','CANCELLED','COMPLETED') DEFAULT 'CONFIRMED',
  `user_id` bigint DEFAULT NULL,
  `parking_spot_id` bigint DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `FKewv68n7xxnaq9qs2fn1ty2lhy` (`spot_id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `FKewv68n7xxnaq9qs2fn1ty2lhy` FOREIGN KEY (`spot_id`) REFERENCES `parkingspots` (`spot_id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES ('Sandesh new','3','Uptown, 6th Avenue (outdoor)','Pay on Spot','300.00','2024-12-25 15:05:00.000000',93,2,'1234567890','COMPLETED',7,NULL),('Sandesh','6','Midtown, 7th Avenue (indoor)','Online Payment','1440.00','2024-12-25 18:09:00.000000',94,3,'1234567890','COMPLETED',7,NULL),('Sandesh UserId Testing','7','Uptown, 6th Avenue (outdoor)','Online Payment','700.00','2024-12-18 06:06:00.000000',95,2,'12345678','COMPLETED',8,NULL),('Test User','5','Midtown, 7th Avenue (indoor)','Pay on Spot','1200.00','2024-12-26 17:33:00.000000',98,3,'1234567890','COMPLETED',2937464,NULL),('Test User','7','Midtown, 7th Avenue (indoor)','Online Payment','1680.00','2024-12-27 17:33:00.000000',99,3,'1234567890','COMPLETED',2937464,NULL),('Sandesh Sanjay Patil','4','Midtown, 7th Avenue (indoor)','Pay on Spot','960.00','2024-12-26 12:54:00.000000',100,3,'1234567','COMPLETED',2003,NULL),('Sandesh Patil','4','Uptown, 6th Avenue (outdoor)','Pay on Spot','400.00','2024-12-26 23:10:00.000000',117,2,'1234567890','CANCELLED',2003,NULL),('Sandesh new','5','Uptown, 6th Avenue (outdoor)','Pay on Spot','500.00','2024-12-27 23:10:00.000000',118,2,'1234567890','CANCELLED',2003,NULL),('Sandesh Patil Updated','3','Midtown, 7th Avenue (indoor)','Pay on Spot','720.00','2024-12-27 12:16:00.000000',119,3,'1234567','COMPLETED',2003,NULL),('Sandesh Sanjay Patil','4','Midtown, 7th Avenue (indoor)','Pay on Spot','960.00','2024-12-27 13:06:00.000000',120,3,'1234567890','CANCELLED',2003,NULL),('sandesh','4','Downtown, 5th Avenue (indoor)','Pay on Spot','400.00','2025-01-17 09:53:00.000000',121,1,'12345678','CANCELLED',2003,NULL),('Sandesh ','3','Downtown, 5th Avenue (indoor)','Pay on Spot','300.00','2025-01-03 12:58:00.000000',122,1,'12345678','CANCELLED',2003,NULL),('sandesh patil','4','Uptown, 6th Avenue (outdoor)','Online Payment','400.00','2025-01-03 10:18:00.000000',123,2,'1234567','CANCELLED',2003,NULL),('Integrate','4','Midtown, 7th Avenue (indoor)','Online Payment','960.00','2025-01-03 16:25:00.000000',124,3,'123345666','CANCELLED',2003,NULL),('Integrate Test','5','Uptown, 6th Avenue (outdoor)','Pay on Spot','500.00','2025-01-03 17:15:00.000000',125,2,'12345678','CANCELLED',2003,NULL),('Integrate new','3','Eastside, 8th Avenue (outdoor)','Pay on Spot','300.00','2025-01-10 17:00:00.000000',126,4,'1234567','CANCELLED',2003,NULL),('sandesh patil','4','Downtown, 5th Avenue (indoor)','Pay on Spot','400.00','2025-01-03 21:45:00.000000',127,1,'12345678','CANCELLED',2003,NULL),('Sandesh new','6','mumbai','Pay on Spot','1200.00','2025-01-16 08:18:00.000000',128,6,'1234567890','CONFIRMED',2003,NULL),('sandesh patil','4','Midtown, 7th Avenue (indoor)','Pay on Spot','960.00','2025-01-04 13:12:00.000000',129,3,'1234567','CANCELLED',1,NULL),('Four integrate ','4','mumbai','Pay on Spot','800.00','2025-01-04 22:22:00.000000',130,7,'12345678','COMPLETED',1,NULL),('Rohan','4','Eastside, 8th Avenue (outdoor)','Pay on Spot','400.00','2025-01-04 14:31:00.000000',131,4,'1234567890','COMPLETED',5,NULL),('sandesh patil','4','Uptown, 6th Avenue (outdoor)','Online Payment','400.00','2025-01-04 21:44:00.000000',132,2,'1234567','COMPLETED',1,NULL),('new booking update changed','3','Downtown, 5th Avenue (indoor)','Pay on Spot','720.00','2025-01-04 22:02:00.000000',133,1,'1234567890','COMPLETED',6,NULL),('Sandesh new','7','Downtown, 5th Avenue (indoor)','Pay on Spot','1050.00','2025-01-07 10:09:00.000000',134,9,'1234567890','CANCELLED',1,NULL),('Sandesh Sanjay Patil','4','Downtown, 5th Avenue (indoor)','Pay on Spot','400.00','2025-01-05 21:32:00.000000',135,1,'1234567890','CANCELLED',1,NULL),('Sandesh Sanjay Patil','4','mumbai','Pay on Spot','800.00','2025-01-05 20:37:00.000000',136,6,'1234567890','CANCELLED',1,NULL),('Sandesh Sanjay Patil','4','Downtown, 5th Avenue (indoor)','Pay on Spot','400.00','2025-01-05 20:43:00.000000',137,1,'1234567890','CONFIRMED',1,NULL),('Test user','4','Uptown, 6th Avenue (outdoor)','Online Payment','400.00','2025-01-05 21:58:00.000000',138,2,'1234567','CANCELLED',13,NULL);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `spot_id` int DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `status` enum('confirmed','pending','completed','canceled') DEFAULT NULL,
  `id` int NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `parking_spot` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `user_id` (`user_id`),
  KEY `spot_id` (`spot_id`),
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`spot_id`) REFERENCES `parkingspots` (`spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evmodel`
--

DROP TABLE IF EXISTS `evmodel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evmodel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_time` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `reservation_date` date DEFAULT NULL,
  `reservation_id` varchar(255) DEFAULT NULL,
  `start_time` varchar(255) DEFAULT NULL,
  `vechile_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evmodel`
--

LOCK TABLES `evmodel` WRITE;
/*!40000 ALTER TABLE `evmodel` DISABLE KEYS */;
INSERT INTO `evmodel` VALUES (2,'23:52','Pune','2025-01-06',NULL,'20:47','543666');
/*!40000 ALTER TABLE `evmodel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parkingspots`
--

DROP TABLE IF EXISTS `parkingspots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parkingspots` (
  `spot_id` int NOT NULL AUTO_INCREMENT,
  `location` varchar(255) DEFAULT NULL,
  `spot_status` varchar(255) DEFAULT 'available',
  `price_per_hour` double NOT NULL,
  `owner_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `availability` varchar(255) DEFAULT NULL,
  `slot` int NOT NULL,
  `spot_name` varchar(255) DEFAULT NULL,
  `station` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`spot_id`),
  KEY `owner_id` (`owner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parkingspots`
--

LOCK TABLES `parkingspots` WRITE;
/*!40000 ALTER TABLE `parkingspots` DISABLE KEYS */;
INSERT INTO `parkingspots` VALUES (1,'Downtown, 5th Avenue (indoor)','available',100,1,NULL,'NO',0,'Premium Downtown Indoor',NULL),(2,'Uptown, 6th Avenue (outdoor)','occupied',100,2,NULL,'YES',0,'Budget Uptown Outdoor',NULL),(3,'Midtown, 7th Avenue (indoor)','available',240,3,NULL,'yes',0,'Luxury Midtown Indoor',NULL),(4,'Eastside, 8th Avenue (outdoor)','available',100,1,NULL,'yes',0,'Standard Eastside Outdoor',NULL),(6,'mumbai','available',200,0,NULL,'YES',5,'New spot','East indoor '),(7,'mumbai','available',200,0,NULL,'yes',5,'New spot updated','East indoor '),(8,'Downtown, 5th Avenue (indoor)','available',150,0,NULL,'yes',0,'Premium Downtown Indoor','Downtown'),(9,'Downtown, 5th Avenue (indoor)','available',150,0,NULL,'YES',0,'Premium Downtown Indoor','Downtown (indoor)'),(10,'Downtown, 5th Avenue (indoor)','available',250,0,NULL,'yes',0,'Premium Downtown Indoor','Downtown'),(11,'Downtown, 5th Avenue (indoor)','available',100,0,NULL,'yes',0,'Premium Downtown Indoor','Downtown indoor '),(12,'Downtown Parking Lot','available',5,1,'Near the main square','yes',10,'Spot A1','Main Station'),(13,'Downtown Parking Lot','available',5,1,'Near the main square','yes',10,'Spot A1','Main Station');
/*!40000 ALTER TABLE `parkingspots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `payment_mode` enum('card','cash','UPI','net_banking') DEFAULT NULL,
  `payment_status` enum('paid','pending','failed') DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `booking_id` (`booking_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spot`
--

DROP TABLE IF EXISTS `spot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spot` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `availability` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `owner_id` int NOT NULL,
  `price_per_hour` double DEFAULT NULL,
  `slot` int NOT NULL,
  `spot_name` varchar(255) DEFAULT NULL,
  `spot_status` varchar(255) DEFAULT NULL,
  `station` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spot`
--

LOCK TABLES `spot` WRITE;
/*!40000 ALTER TABLE `spot` DISABLE KEYS */;
/*!40000 ALTER TABLE `spot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `availability` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `vehicle_model` varchar(255) DEFAULT NULL,
  `vehicle_number` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL DEFAULT '" "',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'latur','ACTIVE','sandesh@gmail.com','sandesh test','$2a$10$Rhgmd1pav5wTzKpcVVJ2.e4EH/jDMFKBG.iQ4cJHtLD0PfTxdzT7q','8668562414','VEHICLE_OWNER','tgv','123456','sandesh New'),(2,'mumbai','ACTIVE','aditya@gmail.com','Aditya Patil','aditya','123456789','VEHICLE_OWNER',NULL,NULL,'AP'),(3,'pune','ACTIVE','example@gmail.com','Example User','examplepass','9876543210','VEHICLE_OWNER','Honda Civic','MH1234','exampleuser'),(9,'Pune','ACTIVE','vaibhav@gmail.com','Vaibhav Chintale','$2a$10$0sZ0xt.uAHKhLLdwWv3lDu7eqSu0wnLwLxI54KdjW6hEJv144x//m','1234567','VEHICLE_OWNER','Volkswagen Polo','123456','vaibhav@gmail.com'),(10,'Mumbai','ACTIVE','rohan@gmail.com','Rohan Panale','$2a$10$HE7F9JokA/qaIwpKJys6leUGUaXOlrsT9vypOuRh87fDOyT/NPxLK','1234567','VEHICLE_OWNER','Volkswagen Polo','24357','rohan@gmail.com'),(11,'Pune ','ACTIVE','mahesh@gmail.com','Mahesh','$2a$10$Id1LxMRoHru4JKQgbV0cq.10cIyBu5Mc2jOb8NHyL43nFmPt7vSOC','12345678','VEHICLE_OWNER','Volkswagen Polo','43556','mahesh@gmail.com'),(13,'mumbai','ACTIVE','test@gmail.com','Test user','$2a$10$A8dW1828P9g7zGOYDC.awuarr9hctTUMWH5at6XkoRO1BcLx8ka6S','234567','VEHICLE_OWNER','Toyota','23456','test@gmail.com'),(14,'Pune','ACTIVE','new@gmail.com','New','$2a$10$ebsnvz4CDpNwV4EaC899/egAXRKNnS5VFCcb5z.FEg20Em0DxJ2AW','1234567','VEHICLE_OWNER','tgv','24357','new@gmail.com');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role` enum('user','admin','owner') DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `mobile_number` (`mobile_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'User One','1234567890','user1@example.com','owner','default_username',NULL),(2,'User Two','0987654321','user2@example.com','owner','default_username',NULL),(3,'User Three','1122334455','user3@example.com','owner','default_username',NULL),(7,'User Testing','12345670','usertest@example.com','user','user','password'),(8,'Sandesh Patil','123456','sandesh@example.com','user','sandesh_patil','password'),(2003,'Sandesh Sanjay Patil','1234569088','sandesh@example.com','user','sandesh','password'),(2937464,'Sandesh Sanjay Patil','123456469088','sandesh@example.com','user','Test_user','password');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-06 10:19:11
