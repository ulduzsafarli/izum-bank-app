CREATE TABLE accounts
(
    id                  SERIAL PRIMARY KEY,
    branch_code         VARCHAR(3)        NOT NULL,
    account_number      VARCHAR(7) UNIQUE NOT NULL,
    account_expire_date DATE              NOT NULL,
    currency_type       VARCHAR(255),
    account_type        VARCHAR(255)      NOT NULL,
    status              VARCHAR(255) DEFAULT 'Active',
    available_balance   NUMERIC(38, 2)    NOT NULL,
    current_balance     NUMERIC(38, 2)    NOT NULL,
    pin                 VARCHAR(255)        NOT NULL,
    transaction_limit NUMERIC(38,2),
    created_at          TIMESTAMP         NOT NULL,
    created_by          VARCHAR(255)      NOT NULL,
    updated_at          TIMESTAMP         NOT NULL,
    updated_by          VARCHAR(255),
    user_id             BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

