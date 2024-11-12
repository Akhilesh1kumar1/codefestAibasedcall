ALTER TABLE loan_disbursed
MODIFY COLUMN disbursed_date DATE;


ALTER TABLE loan_disbursed
ADD COLUMN total_amount_recovered decimal(15,2) default 0.0;



ALTER TABLE loan_application add index idx_created_at(created_at);