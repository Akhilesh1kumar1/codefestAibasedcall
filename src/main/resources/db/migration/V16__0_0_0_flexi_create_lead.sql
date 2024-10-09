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
        'https://console-staging.flexiloans.com/unified/lead' AS `value`,
        1 AS `priority`
    UNION ALL
SELECT
    'CREATE_LEAD',
    'OTHER',
    'method',
    'POST',
    1
UNION ALL
SELECT
    'CREATE_LEAD',
    'HEADER',
    'Authorization',
    'accessToken',
    1
UNION ALL
SELECT
    'CREATE_LEAD',
    'HEADER',
    'Content-Type',
    'application/json',
    1
) AS v
ON
    p.credit_partner_name = 'flexi';

INSERT INTO provider_template (partner_id,source,destination,`type`,`group`) VALUES
	 (13,'$.mobileNumber','/mobile_no','static','CREATE_LEAD'),
	 (13,'$.email','/email','static','CREATE_LEAD'),
	 (13,'$.loanApplication','/loanApplication','static','CREATE_LEAD'),
	 (13,'$.firstName','/first_name','static','CREATE_LEAD'),
	 (13,'$.lastName','/last_name','static','CREATE_LEAD');




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
             'UPLOAD_DOCUMENT' AS `group`,
             'BASE_URL' AS `type`,
             'BASE_URL' AS `key`,
             'https://console-staging.flexiloans.com/documentservice' AS `value`,
             1 AS `priority`
         UNION ALL
     SELECT
         'UPLOAD_DOCUMENT',
         'OTHER',
         'method',
         'POST',
         1
     UNION ALL
     SELECT
         'UPLOAD_DOCUMENT',
         'HEADER',
         'Authorization',
         'accessToken',
         1
     UNION ALL
     SELECT
         'UPLOAD_DOCUMENT',
         'HEADER',
         'Content-Type',
         'multipart/form-data',
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
             'PENDING_DOCUMENT' AS `group`,
             'BASE_URL' AS `type`,
             'BASE_URL' AS `key`,
             'https://console-staging.flexiloans.com/documentservice/pending' AS `value`,
             1 AS `priority`
         UNION ALL
     SELECT
         'PENDING_DOCUMENT',
         'OTHER',
         'method',
         'GET',
         1
      UNION ALL
          SELECT
              'PENDING_DOCUMENT',
              'QUERY_PARAM',
              'loan_code',
              'clientLoanId',
              1
     UNION ALL
     SELECT
         'PENDING_DOCUMENT',
         'HEADER',
         'Authorization',
         'accessToken',
         1
     UNION ALL
     SELECT
         'PENDING_DOCUMENT',
         'HEADER',
         'Content-Type',
         'application/json',
         1
     ) AS v
     ON
         p.credit_partner_name = 'flexi';


INSERT INTO provider_template (partner_id,source,destination,`type`,`group`) VALUES
	 (13,'$.customerCategory','/customer_category','static','LOAN__STATUS_WEBHOOK'),
	 (13,'ARVIND_SHIPROCKET_HERO','/product_id','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.disbursementAccounts','/disbursement_accounts','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.clientCustomerId','/client_customer_id','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.firstName','/first_name','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.lastName','/last_name','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.dateOfBirth','/date_of_birth','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.gender','/gender','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.primaryBorrowerType','/primary_borrower_type','static','LOAN__STATUS_WEBHOOK'),
	 (13,'$.currentAddress','/current_address','static','LOAN__STATUS_WEBHOOK');