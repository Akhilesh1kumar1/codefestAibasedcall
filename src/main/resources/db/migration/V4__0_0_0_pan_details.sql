-- PAN details

CREATE TABLE IF NOT EXISTS `pan_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `pan` varchar(50) DEFAULT NULL,
  `is_pan_verified` bit(1) DEFAULT NULL,
  `pan_image_link_1` varchar(255) DEFAULT NULL,
  `pan_image_link_2` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE `pan_details` ADD CONSTRAINT pan_unique_constraint UNIQUE (pan);
ALTER TABLE `pan_details` ADD INDEX idx_pan_user_id (pan, user_id);