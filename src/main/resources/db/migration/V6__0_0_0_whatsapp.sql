-- Provider details

CREATE TABLE IF NOT EXISTS `whatsapp_api_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `message_id` varchar(255) DEFAULT NULL,
  `remarks` text DEFAULT NULL,
  `internal_id` varchar(255) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT 'LEAD',
  `sr_company_id` bigint NOT NULL,
  `created_at` DATETIME DEFAULT NULL,
     `created_by` varchar(255) DEFAULT NULL,
     `updated_at` DATETIME DEFAULT NULL,
     `updated_by` varchar(255) DEFAULT NULL,
     `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg1xmqxbnyswqft5jvsj9bsf0g` (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




