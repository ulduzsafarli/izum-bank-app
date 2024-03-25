CREATE TABLE accounts
(
    id                  SERIAL PRIMARY KEY,
    branch_code         VARCHAR(255),
    account_number      VARCHAR(255) UNIQUE,
    account_open_date   TIMESTAMP,
    account_expire_date TIMESTAMP,
    iban                VARCHAR(255),
    swift               VARCHAR(255),
    currency            VARCHAR(255),
    account_type        VARCHAR(255),
    status              VARCHAR(255),
    available_balance   NUMERIC(38, 2),
    current_balance     NUMERIC(38, 2),
    blocked_amount      NUMERIC(38, 2),
    pin                 VARCHAR(255),
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- customer_id         bigint REFERENCES customers (id),
