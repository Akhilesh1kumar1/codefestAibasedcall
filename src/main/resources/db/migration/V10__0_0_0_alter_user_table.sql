ALTER TABLE `provider_url_config`
CHANGE `created_at` `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
CHANGE `created_by` `created_by` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'SYSTEM',
CHANGE `updated_at` `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
CHANGE `updated_by` `updated_by` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'SYSTEM',
CHANGE `is_enabled` `is_enabled` BIT(1) NULL DEFAULT b'1';
--

-- Alter `provider_template` for default values
ALTER TABLE `provider_template`
CHANGE `created_at` `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
CHANGE `created_by` `created_by` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'SYSTEM',
CHANGE `updated_at` `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
CHANGE `updated_by` `updated_by` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'SYSTEM',
CHANGE `is_enabled` `is_enabled` BIT(1) NULL DEFAULT b'1';


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
        'https://colend-uat-01-api.go-yubi.in/colending/clients/arvind/api/v2/loans' AS `value`,
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
    'api-key',
    'accessToken',
    1
UNION ALL
SELECT
    'CREATE_LEAD',
    'HEADER',
    'Product-Id',
    'accountId',
    1
) AS v
ON
    p.credit_partner_name = 'yubi';

INSERT INTO capital.provider_template (partner_id,source,destination,`type`,`group`) VALUES
	 (4,'$.customerCategory','/customer_category','static','CREATE_LEAD'),
	 (4,'ARVIND_SHIPROCKET_HERO','/product_id','static','CREATE_LEAD'),
	 (4,'$.disbursementAccounts','/disbursement_accounts','static','CREATE_LEAD'),
	 (4,'$.clientCustomerId','/client_customer_id','static','CREATE_LEAD'),
	 (4,'$.firstName','/first_name','static','CREATE_LEAD'),
	 (4,'$.lastName','/last_name','static','CREATE_LEAD'),
	 (4,'$.dateOfBirth','/date_of_birth','static','CREATE_LEAD'),
	 (4,'$.gender','/gender','static','CREATE_LEAD'),
	 (4,'$.primaryBorrowerType','/primary_borrower_type','static','CREATE_LEAD'),
	 (4,'$.currentAddress','/current_address','static','CREATE_LEAD');
INSERT INTO capital.provider_template (partner_id,source,destination,`type`,`group`) VALUES
	 (4,'$.currentCity','/current_city','static','CREATE_LEAD'),
	 (4,'$.currentState','/current_state','static','CREATE_LEAD'),
	 (4,'$.currentPincode','/current_pincode','static','CREATE_LEAD'),
	 (4,'$.mobileNumber','/mobile_number','static','CREATE_LEAD'),
	 (4,'$.panNumber','/pan_number','static','CREATE_LEAD'),
	 (4,'$.applicationId','/application_id','static','CREATE_LEAD'),
	 (4,'$.clientLoanId','/client_loan_id','static','CREATE_LEAD'),
	 (4,'$.category','/category','static','CREATE_LEAD'),
	 (4,'$.subCategory','/sub_category','static','CREATE_LEAD'),
	 (4,'$.principalAmount','/principal_amount','static','CREATE_LEAD');
INSERT INTO capital.provider_template (partner_id,source,destination,`type`,`group`) VALUES
	 (4,'$.email','/email','static','CREATE_LEAD'),
	 (4,'$.permanentAddress','/permanent_address','static','CREATE_LEAD'),
	 (4,'$.permanentCity','/permanent_city','static','CREATE_LEAD'),
	 (4,'$.permanentState','/permanent_state','static','CREATE_LEAD'),
	 (4,'$.permanentPincode','/permanent_pincode','static','CREATE_LEAD'),
	 (4,'$.nameOfBureau','/name_of_bureau','static','CREATE_LEAD'),
	 (4,'$.bureauScore','/bureau_score','static','CREATE_LEAD'),
	 (4,'Single','/disbursement_type','static','CREATE_LEAD'),
	 (4,'$.interestRate','/interest_rate','static','CREATE_LEAD'),
	 (4,'$.tenure','/tenure','static','CREATE_LEAD');
INSERT INTO capital.provider_template (partner_id,source,destination,`type`,`group`) VALUES
	 (4,'$.numberOfRepayments','/number_of_repayments','static','CREATE_LEAD'),
	 (4,'$.tenureFrequency','/tenure_frequency','static','CREATE_LEAD'),
	 (4,'$.repaymentFrequency','/repayment_frequency','static','CREATE_LEAD'),
	 (4,'$.business','/business','static','CREATE_LEAD'),
	 (4,'$.client_loan_id','/clientLoanId','static','CREATE_LEAD_RESPONSE'),
	 (4,'$.status','/status','static','CREATE_LEAD_RESPONSE');
