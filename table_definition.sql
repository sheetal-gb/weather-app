CREATE TABLE `testschema`.`weather` (
   `id` int NOT NULL AUTO_INCREMENT,
   `sensor_id` varchar(45) DEFAULT NULL,
   `temperature` int DEFAULT NULL,
   `location` varchar(45) DEFAULT NULL,
   `reading_time` datetime DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
