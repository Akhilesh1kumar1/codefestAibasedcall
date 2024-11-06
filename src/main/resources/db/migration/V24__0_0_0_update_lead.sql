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
        'CREATE_LEAD' AS `group`,
        'BASE_URL' AS `type`,
        'BASE_URL' AS `key`,
        'https://console-staging.flexiloans.com/unified/lead/' AS `value`,
        1 AS `priority`
    UNION ALL
SELECT
    'UPDATE_LEAD',
    'OTHER',
    'method',
    'POST',
    1
UNION ALL
          SELECT
              'FETCH_SANCTIONED_OFFER',
              'PATH_VARIABLE',
              'leadCode',
              'leadCode',
              1
UNION ALL
SELECT
    'UPDATE_LEAD',
    'HEADER',
    'Authorization',
    'accessToken',
    1
UNION ALL
SELECT
    'UPDATE_LEAD',
    'HEADER',
    'Content-Type',
    'application/json',
    1
) AS v
ON
    p.credit_partner_name = 'flexi';