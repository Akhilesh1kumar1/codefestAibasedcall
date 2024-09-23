
CREATE TABLE IF NOT EXISTS underwriting_config (
  `id` bigint NOT NULL AUTO_INCREMENT,
    group_name VARCHAR(255),
    name VARCHAR(255),
    `value` DOUBLE,
    score DOUBLE,
    `condition` VARCHAR(255),
    weightage DOUBLE,
  `created_at` DATETIME DEFAULT NULL,
     `created_by` varchar(255) DEFAULT NULL,
     `updated_at` DATETIME DEFAULT NULL,
     `updated_by` varchar(255) DEFAULT NULL,
     `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




