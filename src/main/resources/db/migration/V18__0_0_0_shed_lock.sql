CREATE TABLE IF NOT EXISTS shedlock (
    name VARCHAR(64) NOT NULL,
    lock_until TIMESTAMP(3) NULL,
    locked_at TIMESTAMP(3) NULL,
    locked_by VARCHAR(255) NULL,
    PRIMARY KEY (name)
);


ALTER TABLE user
ADD COLUMN current_account_available TINYINT(1) DEFAULT 0;