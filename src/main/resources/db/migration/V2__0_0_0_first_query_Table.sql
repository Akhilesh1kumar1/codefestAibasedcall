CREATE TABLE IF NOT EXISTS `account_type`
 (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_type_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
   `created_by` varchar(255) DEFAULT NULL,
   `updated_at` datetime(6) DEFAULT NULL,
   `updated_by` varchar(255) DEFAULT NULL,
   `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

ALTER TABLE `account_type` ADD CONSTRAINT account_type_name_constraint UNIQUE (account_type_name);


CREATE TABLE  IF NOT EXISTS  `adhar_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `adhar_number` varchar(50) DEFAULT NULL,
  `date_of_birth` varchar(255) DEFAULT NULL,
  `is_adhar_verified` bit(1) DEFAULT NULL,
  `name_on_adhar` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `adhar_image_link_1` varchar(255) DEFAULT NULL,
   `adhar_image_link_2` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE `adhar_details` ADD CONSTRAINT adhar_number_constraint UNIQUE (adhar_number);
ALTER TABLE adhar_details ADD INDEX idx_adhar_number_user_id (adhar_number, user_id);


CREATE TABLE if not exists `verification` (
  `id` binary(16) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `callback` varchar(255) DEFAULT NULL,
    `channel` varchar(255) DEFAULT NULL,
    `data` varchar(255) DEFAULT NULL,
    `expires_at` datetime(6) DEFAULT NULL,
    `failed_counter` int DEFAULT NULL,
    `request_counter` int DEFAULT NULL,
    `type` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;



CREATE TABLE if not exists `bank` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `image_link` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;
ALTER TABLE `bank` ADD CONSTRAINT bank_name_constraint UNIQUE (bank_name);

 CREATE TABLE if not exists `base_credit_partners` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `credit_partner_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `image_link` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
ALTER TABLE `base_credit_partners` ADD CONSTRAINT credit_partner_name_constraint UNIQUE (credit_partner_name);

CREATE TABLE if not exists `company_details` (
  `id` binary(16) NOT NULL,
   `tenant_capital_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
  `adhar_id` bigint DEFAULT NULL,
  `company_type` smallint DEFAULT NULL,
  `pan_id` bigint DEFAULT NULL,
  `proof_image_link` varchar(255) DEFAULT NULL,
  `proof_of_identity` smallint DEFAULT NULL,
   `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE company_details ADD CONSTRAINT tenant_capital_id_user_id_constraint UNIQUE (tenant_capital_id, user_id);


 CREATE TABLE if not exists `rsa_keys` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  `private_key` tinytext,
  `public_key` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;


CREATE TABLE `tenant_bank_details` (
  `id` binary(16) NOT NULL,
  `account_holder_name` varchar(255) DEFAULT NULL,
  `account_number` varchar(255) DEFAULT NULL,
  `account_statement_link` varchar(255) DEFAULT NULL,
  `base_bank_id` bigint DEFAULT NULL,
  `ifsc_code` varchar(255) DEFAULT NULL,
  `is_account_verified` bit(1) DEFAULT NULL,
  `statement_password` varchar(255) DEFAULT NULL,
  `tenant_capital_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

ALTER TABLE tenant_bank_details ADD CONSTRAINT account_number_constraint UNIQUE (account_number);
ALTER TABLE tenant_bank_details ADD INDEX idx_tenant_capital_id_user_id_adhar_number (tenant_capital_id, user_id,account_number);

CREATE TABLE if not exists `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_email_verified` bit(1) DEFAULT NULL,
  `is_mobile_verified` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `sr_user_id` bigint  DEFAULT NULL,
  `sr_company_id` bigint DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

ALTER TABLE user ADD CONSTRAINT sr_user_id_constraint UNIQUE (sr_user_id);
ALTER TABLE user ADD CONSTRAINT email_constraint UNIQUE (email);

