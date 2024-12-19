ALTER TABLE `loan_application`
ADD COLUMN `internal_loan_id` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
ADD INDEX internal_loan_id (internal_loan_id);
