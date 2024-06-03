CREATE TABLE if not exists `company_kyc_details` (
  `id` binary(16) NOT NULL,
   `tenant_capital_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
  `adhar_id` bigint DEFAULT NULL,
  `company_type` varchar(100) DEFAULT NULL,
  `pan_id` bigint DEFAULT NULL,
  `proof_image_link` varchar(255) DEFAULT NULL,
  `proof_of_identity` varchar(100) DEFAULT NULL,
   `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;





