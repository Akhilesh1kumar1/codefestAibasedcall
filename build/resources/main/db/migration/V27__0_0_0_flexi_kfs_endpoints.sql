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
        'FETCH_KFS' AS `group`,
        'BASE_URL' AS `type`,
        'BASE_URL' AS `key`,
        'https://console.flexiloans.com/psc/loan/{0}/kfs/download' AS `value`,
        1 AS `priority`
    UNION ALL
SELECT
    'FETCH_KFS',
    'OTHER',
    'method',
    'GET',
    1
UNION ALL
SELECT
    'FETCH_KFS',
    'HEADER',
    'Authorization',
    'accessToken',
    1
UNION ALL
SELECT
    'FETCH_KFS',
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
        'REJECT_OFFER' AS `group`,
        'BASE_URL' AS `type`,
        'BASE_URL' AS `key`,
        'https://console.flexiloans.com/psc/sanction/reject' AS `value`,
        1 AS `priority`
    UNION ALL
SELECT
    'REJECT_OFFER',
    'OTHER',
    'method',
    'PUT',
    1
UNION ALL
SELECT
    'REJECT_OFFER',
    'HEADER',
    'Authorization',
    'accessToken',
    1
UNION ALL
SELECT
    'REJECT_OFFER',
    'HEADER',
    'Content-Type',
    'application/json',
    1
) AS v
ON
    p.credit_partner_name = 'flexi';