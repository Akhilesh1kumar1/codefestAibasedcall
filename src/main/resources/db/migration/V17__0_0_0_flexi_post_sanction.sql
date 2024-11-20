INSERT INTO provider_url_config(
    `partner_id`,
    `group`,
    `type`,
    `key`,
    `value`,
    `priority`
)
SELECT
    p.id,
    v.group,
    v.type,
    v.key,
    v.value,
    v.priority
FROM
    base_credit_partners p
JOIN(
    SELECT
        'FETCH_DISBURSED_DETAILS' AS `group`,
        'BASE_URL' AS `type`,
        'BASE_URL' AS `key`,
        'https://console-staging.flexiloans.com/unified/loanapplication/{0}/loandisburse' AS `value`,
        1 AS `priority`
    UNION ALL
SELECT
    'FETCH_DISBURSED_DETAILS',
    'OTHER',
    'method',
    'GET',
    1
UNION ALL
SELECT
    'FETCH_DISBURSED_DETAILS',
    'HEADER',
    'Authorization',
    'accessToken',
    1
UNION ALL
SELECT
    'FETCH_DISBURSED_DETAILS',
    'HEADER',
    'Content-Type',
    'application/json',
    1
) AS v
ON
    p.credit_partner_name = 'flexi';

	 INSERT INTO provider_url_config(
         `partner_id`,
         `group`,
         `type`,
         `key`,
         `value`,
         `priority`
     )
     SELECT
         p.id,
         v.group,
         v.type,
         v.key,
         v.value,
         v.priority
     FROM
         base_credit_partners p
     JOIN(
         SELECT
             'FETCH_SANCTIONED_OFFER' AS `group`,
             'BASE_URL' AS `type`,
             'BASE_URL' AS `key`,
             'https://console-staging.flexiloans.com/psc/loanoffer/' AS `value`,
             1 AS `priority`
         UNION ALL
     SELECT
         'FETCH_SANCTIONED_OFFER',
         'OTHER',
         'method',
         'GET',
         1
     UNION ALL
          SELECT
              'FETCH_SANCTIONED_OFFER',
              'PATH_VARIABLE',
              'clientLoanId',
              'clientLoanId',
              1
     UNION ALL
     SELECT
         'FETCH_SANCTIONED_OFFER',
         'HEADER',
         'Authorization',
         'accessToken',
         1
     UNION ALL
     SELECT
         'FETCH_SANCTIONED_OFFER',
         'HEADER',
         'Content-Type',
         'application/json',
         1
     ) AS v
     ON
         p.credit_partner_name = 'flexi';


INSERT INTO provider_url_config(
         `partner_id`,
         `group`,
         `type`,
         `key`,
         `value`,
         `priority`
     )
     SELECT
         p.id,
         v.group,
         v.type,
         v.key,
         v.value,
         v.priority
     FROM
         base_credit_partners p
     JOIN(
         SELECT
             'ACCEPT_SECTION_OFFER' AS `group`,
             'BASE_URL' AS `type`,
             'BASE_URL' AS `key`,
             'https://console-staging.flexiloans.com/psc/sanction/accept/' AS `value`,
             1 AS `priority`
         UNION ALL
     SELECT
         'ACCEPT_SECTION_OFFER',
         'OTHER',
         'method',
         'GET',
         1
      UNION ALL
          SELECT
              'ACCEPT_SECTION_OFFER',
              'PATH_VARIABLE',
              'sanctionCode',
              'sanctionCode',
              1
     UNION ALL
     SELECT
         'ACCEPT_SECTION_OFFER',
         'HEADER',
         'Authorization',
         'accessToken',
         1
     UNION ALL
     SELECT
         'ACCEPT_SECTION_OFFER',
         'HEADER',
         'Content-Type',
         'application/json',
         1
     ) AS v
     ON
         p.credit_partner_name = 'flexi';


CREATE TABLE IF NOT EXISTS  `kafka_inbound_messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `message_id` varchar(255) DEFAULT NULL,
  `handler_name` varchar(255) DEFAULT NULL,
  `group_id` varchar(255) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `lock_expires_at` bigint DEFAULT NULL,
  `message_state` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_index` (`message_id`,`handler_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


         CREATE TABLE IF NOT EXISTS  `kafka_outbound_messages` (
           `id` bigint NOT NULL AUTO_INCREMENT,
           `message_id` varchar(255) DEFAULT NULL,
           `group_id` varchar(255) DEFAULT NULL,
           `topic` varchar(255) DEFAULT NULL,
           `payload` mediumtext,
           `event_type` varchar(255) DEFAULT NULL,
           `header_str` mediumtext,
           `sent` bit(1) NOT NULL,
           `retries` int NOT NULL,
           `status` varchar(1023) DEFAULT NULL,
           `created_at` datetime DEFAULT NULL,
           PRIMARY KEY (`id`),
           KEY `idx_outbound_messages_sent` (`sent`)
         ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

         CREATE TABLE IF NOT EXISTS  `kafka_jpa_lock` (
           `lock_id` varchar(255) NOT NULL,
           `expires_at` varchar(45) DEFAULT NULL,
           PRIMARY KEY (`lock_id`)
         ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;