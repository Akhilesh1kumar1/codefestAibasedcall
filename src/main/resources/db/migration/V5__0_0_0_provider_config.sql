-- Provider details

CREATE TABLE `provider_url_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `partner_id` int DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `key` varchar(255) DEFAULT NULL,
  `value` text,
  `priority` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
     `created_by` varchar(255) DEFAULT NULL,
     `updated_at` datetime(6) DEFAULT NULL,
     `updated_by` varchar(255) DEFAULT NULL,
     `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg1xmqxbnyswqft5jvsj9bsf0g` (`partner_id`),
  KEY `channel_id_group` (`partner_id`,`group`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `provider_template` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `partner_id` bigint DEFAULT NULL,
  `source` varchar(300) DEFAULT NULL,
  `destination` varchar(300) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL,
 `created_at` datetime(6) DEFAULT NULL,
      `created_by` varchar(255) DEFAULT NULL,
      `updated_at` datetime(6) DEFAULT NULL,
      `updated_by` varchar(255) DEFAULT NULL,
      `is_enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa5e29k32eppre1eyhmtqa5ayy` (`partner_id`,`group`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



INSERT INTO `provider_template` (`id`,`partner_id`,`source`,`destination`,`type`,`group`,`created_at`,`created_by`,`updated_at`,`updated_by`,`is_enabled`) VALUES (1,1,'$.token','/token','static','VALIDATE_TOKEN','2022-04-28 13:18:48',-1,'2022-04-28 13:18:48',-1,1);
INSERT INTO `provider_template` (`id`,`partner_id`,`source`,`destination`,`type`,`group`,`created_at`,`created_by`,`updated_at`,`updated_by`,`is_enabled`) VALUES (2,1,'1','/validate_token_request','static','VALIDATE_TOKEN','2022-04-28 13:18:48',-1,'2022-04-28 13:18:48',-1,1);

INSERT INTO `provider_url_config` (`partner_id`,`group`,`type`,`key`,`value`,`priority`,`created_at`,`created_by`,`updated_at`,`updated_by`,`is_enabled`) VALUES (1,'VALIDATE_TOKEN','OTHER','method','POST',1,'2022-06-15 13:05:59',-1,'2022-06-15 13:05:59',-1,1);


INSERT INTO `provider_template` (`partner_id`,`source`,`destination`,`type`,`group`,`created_at`,`created_by`,`updated_at`,`updated_by`,`is_enabled`) VALUES (1,'$.code','/code','static','VALIDATE_TOKEN_RESPONSE','2022-04-28 13:18:48',-1,'2022-04-28 13:18:48',-1,1);
INSERT INTO `provider_template` (`partner_id`,`source`,`destination`,`type`,`group`,`created_at`,`created_by`,`updated_at`,`updated_by`,`is_enabled`) VALUES (1,'$.msg','/msg','static','VALIDATE_TOKEN_RESPONSE','2022-04-28 13:18:48',-1,'2022-04-28 13:18:48',-1,1);

