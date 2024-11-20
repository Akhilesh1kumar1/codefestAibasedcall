ALTER TABLE loan_disbursed
MODIFY COLUMN disbursed_date DATE,
ADD COLUMN emi_amount decimal(15,2) default 0.0;



ALTER TABLE loan_application_status
ADD COLUMN sanction_code varchar(255) default null,
ADD COLUMN total_recoverable_amount decimal(15,2) default 0.0;

ALTER TABLE loan_application
ADD COLUMN vendor_status varchar(255) default null;



ALTER TABLE loan_application add index idx_created_at(created_at);