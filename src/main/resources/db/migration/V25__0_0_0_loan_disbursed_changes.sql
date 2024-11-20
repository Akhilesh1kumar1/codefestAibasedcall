ALTER TABLE loan_disbursed
ADD COLUMN loan_id BINARY(16) NOT NULL,
ADD INDEX idx_loan_id (loan_id),
add INDEX idx_disburse_date(disbursed_date);


ALTER TABLE loan_application add index idx_external_loan_id(external_loan_id);