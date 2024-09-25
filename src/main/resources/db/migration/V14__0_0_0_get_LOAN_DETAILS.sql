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
        'https://colend-uat-01-api.go-yubi.in/colending/clients/arvind/api/v2/loans/' AS `value`,
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
    'api-key',
    'accessToken',
    1
UNION ALL
SELECT
    'GET_LOAN',
    'HEADER',
    'Product-Id',
    'accountId',
    1
) AS v
ON
    p.credit_partner_name = 'yubi';

