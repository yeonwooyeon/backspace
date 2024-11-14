USE vacancy;

Drop table wish;

CREATE TABLE `wish` (
  `info_no` int NOT NULL,
  `id` bigint NOT NULL,
  `agent_no` int NOT NULL AUTO_INCREMENT,
  `wish_date` timestamp NULL DEFAULT NULL,
  `wish_status` varchar(10) DEFAULT NULL,
  `wish_regi` varchar(15) DEFAULT NULL,
  `wish_modi` varchar(15) DEFAULT NULL,
  `wish_day` timestamp NULL DEFAULT NULL,
  `wish_modiday` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`agent_no`),
  KEY `fk_wish_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE cart_property (
    cart_no INT AUTO_INCREMENT PRIMARY KEY, 
    cart_id INT NOT NULL,            
    property_id INT NOT NULL,           
    FOREIGN KEY (property_id) REFERENCES Pp_info(info_no) ON DELETE CASCADE 
);  `wish_modiday` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`agent_no`),
  KEY `fk_wish_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
