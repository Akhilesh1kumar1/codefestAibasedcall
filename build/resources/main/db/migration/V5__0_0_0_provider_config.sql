-- Provider details

CREATE TABLE IF NOT EXISTS `provider_url_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `partner_id` int DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `key` varchar(255) DEFAULT NULL,
  `value` text,
  `priority` int DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
     `created_by` varchar(255) DEFAULT NULL,
     `updated_at` DATETIME DEFAULT NULL,
     `updated_by` varchar(255) DEFAULT NULL,
     `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg1xmqxbnyswqft5jvsj9bsf0g` (`partner_id`),
  KEY `channel_id_group` (`partner_id`,`group`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE IF NOT EXISTS `provider_template` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `partner_id` bigint DEFAULT NULL,
  `source` varchar(300) DEFAULT NULL,
  `destination` varchar(300) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL,
 `created_at` DATETIME DEFAULT NULL,
      `created_by` varchar(255) DEFAULT NULL,
      `updated_at` DATETIME DEFAULT NULL,
      `updated_by` varchar(255) DEFAULT NULL,
      `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa5e29k32eppre1eyhmtqa5ayy` (`partner_id`,`group`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





CREATE TABLE IF NOT EXISTS loan_offer (
    id binary(16) NOT NULL,
    sr_company_id BIGINT,
    loan_vendor_id BIGINT,
    loan_type VARCHAR(255),
    pre_approved BOOLEAN,
    pre_approved_loan_amount DECIMAL(15, 2),
    interest_rate DOUBLE,
    loan_duration INT,
    status VARCHAR(255),
    start_date DATE,
    end_date DATE,
    is_loan_applied  bit(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
          `created_by` varchar(255) DEFAULT NULL,
          `updated_at` DATETIME DEFAULT NULL,
          `updated_by` varchar(255) DEFAULT NULL,
          `is_enabled` bit(1) DEFAULT NULL,
          PRIMARY KEY (`id`),
          INDEX sr_company_id_loan_vendor_id(sr_company_id, loan_vendor_id)

);






CREATE TABLE IF NOT EXISTS loan_application (
    id binary(16) NOT NULL,
    sr_company_id BIGINT NOT NULL,
    loan_vendor_id BIGINT NOT NULL,
    loan_amount_requested DECIMAL(15, 2) NOT NULL,
    loan_status VARCHAR(20) NOT NULL,
    loan_offer_id binary(36) default NULL,
    loan_duration int not null default 0,
    loan_type varchar(200) default null,
     `created_at` DATETIME DEFAULT NULL,
                  `created_by` varchar(255) DEFAULT NULL,
                  `updated_at` DATETIME DEFAULT NULL,
                  `updated_by` varchar(255) DEFAULT NULL,
                  `is_enabled` bit(1) DEFAULT NULL,
                  PRIMARY KEY (`id`),
                  INDEX idx_sr_company_id (sr_company_id)
);

CREATE TABLE IF NOT EXISTS loan_application_status (
    id bigint(16)  NOT NULL AUTO_INCREMENT,
    sr_company_id BIGINT,
    loan_vendor_id BIGINT,
    loan_amount_requested DECIMAL(19,2),
    loan_status VARCHAR(255),
    loan_offer_id BINARY(16),
    loan_duration INT,
    loan_id BINARY(16),
    vendor_loan_id VARCHAR(255),
    total_disbursed_amount decimal(15,2) NOT NULL,
    vendor_status varchar(255) default null,
    loan_amount_approved DECIMAL(19,2),
    interest_amount_at_sanction DECIMAL(19,2),
    interest_rate DOUBLE,
    start_date DATE,
    end_date DATE,
    comment VARCHAR(1000),
       `created_at` DATETIME DEFAULT NULL,
                      `created_by` varchar(255) DEFAULT NULL,
                      `updated_at` DATETIME DEFAULT NULL,
                      `updated_by` varchar(255) DEFAULT NULL,
                      `is_enabled` bit(1) DEFAULT NULL,
    PRIMARY KEY (id),
    index loan_id(loan_id)
);
CREATE TABLE  IF NOT EXISTS `verification` (
  `id` binary(16) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `callback` varchar(255) DEFAULT NULL,
  `channel` varchar(255) DEFAULT NULL,
  `data` varchar(255) DEFAULT NULL,
  `expires_at` DATETIME DEFAULT NULL,
  `failed_counter` int DEFAULT NULL,
  `request_counter` int DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `sr_company_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `task` (
  `id` bigint(16)  NOT NULL AUTO_INCREMENT,
  `request_id` varchar(255) DEFAULT NULL,
  `group_id` bigint(16) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `retries` int DEFAULT NULL,
  `last_try_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_request_id` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





CREATE TABLE IF NOT EXISTS loan_disbursed (
    id BIGINT NOT NULL AUTO_INCREMENT,
    loan_application_status_id BIGINT,
    loan_amount_disbursed DECIMAL(19,2),
    interest_rate_at_disbursal DOUBLE,
    interest_amount_at_disbursal DECIMAL(19,2),
    duration_at_disbursal INT,
    vendor_disbursed_id VARCHAR(255),
    `created_at` DATETIME DEFAULT NULL,
      `created_by` varchar(255) DEFAULT NULL,
      `updated_at` DATETIME DEFAULT NULL,
      `updated_by` varchar(255) DEFAULT NULL,
      `is_enabled` bit(1) DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX idx_loan_application_status_id (loan_application_status_id)
);


CREATE TABLE IF NOT EXISTS `account_type`
 (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_type_name` varchar(255) NOT NULL UNIQUE,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
   `created_by` varchar(255) DEFAULT NULL,
   `updated_at` DATETIME DEFAULT NULL,
   `updated_by` varchar(255) DEFAULT NULL,
   `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;



CREATE TABLE if not exists `bank` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) NOT NULL UNIQUE,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `image_link` varchar(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

 CREATE TABLE if not exists `base_credit_partners` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `credit_partner_name` varchar(255) NOT NULL UNIQUE,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `image_link` varchar(255) DEFAULT NULL,
  provide_rbf bit(1) default 1,
  provide_pre_approved_loan bit(1) default 1,
  provide_term_loan bit(1) default 1,
    invoice_financing bit(1) default 1,
  loan_against_property bit(1) default 1,
  line_of_credit bit(1) default 1,
  business_credit_card bit(1) default 1,
  gst bit(1) default 1,
  `created_at` DATETIME DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;



 CREATE TABLE if not exists `rsa_keys` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  `private_key` tinytext,
  `public_key` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;





CREATE TABLE if not exists `user` (
    id bigint NOT NULL AUTO_INCREMENT,
    sr_user_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(255),
    middle_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) not null UNIQUE,
    is_email_verified TINYINT(1) DEFAULT 0,
    country_code VARCHAR(10) DEFAULT '+91',
    mobile VARCHAR(100),
    comments TEXT,
    sr_company_id BIGINT,
    entity_type varchar(255) ,
    pan_number varchar(100),
    company_name text,
    is_mobile_verified bit(1) DEFAULT 0,
    is_accepted TINYINT(1) DEFAULT 0,
     `created_at` DATETIME DEFAULT NULL,
              `created_by` varchar(255) DEFAULT NULL,
              `updated_at` DATETIME DEFAULT NULL,
              `updated_by` varchar(255) DEFAULT NULL,
              `is_enabled` bit(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
     index sr_company_id(`sr_company_id`)
)ENGINE=InnoDB AUTO_INCREMENT=0;



