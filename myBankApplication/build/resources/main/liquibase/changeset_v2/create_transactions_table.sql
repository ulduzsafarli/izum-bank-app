CREATE TABLE transactions
(
    id               SERIAL PRIMARY KEY,
    description      VARCHAR(255),
    amount           NUMERIC(38, 2),
    transaction_date TIMESTAMP,
    type             VARCHAR(255),
    status           VARCHAR(255),
    operator_id      BIGINT,
    transaction_uuid VARCHAR(255) UNIQUE,
    account_id       BIGINT,
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);
