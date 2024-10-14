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
        'GET_TOKEN' AS `group`,
        'BASE_URL' AS `type`,
        'BASE_URL' AS `key`,
        'https://auth.flexiloans.com/oauth2/token' AS `value`,
        1 AS `priority`
    UNION ALL
    SELECT
        'GET_TOKEN',
        'HEADER',
        'Content-Type',
        'application/x-www-form-urlencoded',
        1
    UNION ALL
    SELECT
        'GET_TOKEN',
        'HEADER',
        'Authorization',
        'Basic NjFzNzdkOGMzdW90bXZqcjBmZXE1ZWk0djoxMWpldjE5YWZkNTE3cTNuZnI4NW1zOTRsOWEzZmdrNzA4OWZwdGY0ZTBzOWZqaWM0ajcx',
        1
    UNION ALL
    SELECT
        'GET_TOKEN',
        'OTHER',
        'method',
        'POST',
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
        'VALIDATE_LOAN' AS `group`,
        'BASE_URL' AS `type`,
        'BASE_URL' AS `key`,
        'https://console-staging.flexiloans.com/unified/v2/loanapplicant/{0}/dedupecheck' AS `value`,
        1 AS `priority`
UNION ALL
    SELECT
        'VALIDATE_LOAN',
        'HEADER',
        'Content-Type',
        'application/json',
        1
UNION ALL
SELECT
    'VALIDATE_LOAN',
    'HEADER',
    'Authorization',
    'accessToken',
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
        'GET_LOAN' AS `group`,
        'BASE_URL' AS `type`,
        'BASE_URL' AS `key`,
        'https://console-staging.flexiloans.com/state/v2/loan/' AS `value`,
        1 AS `priority`
    UNION ALL
SELECT
    'GET_LOAN',
    'PATH_VARIABLE',
    'clientLoanId',
    'clientLoanId',
    1
UNION ALL
SELECT
    'GET_LOAN',
    'HEADER',
    'Authorization',
    'accessToken',
    1
UNION ALL
SELECT
    'GET_LOAN',
    'HEADER',
    'Content-Type',
    'application/json',
    1
) AS v
ON
    p.credit_partner_name = 'flexi';



