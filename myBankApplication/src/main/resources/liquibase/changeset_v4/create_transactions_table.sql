CREATE TABLE transactions
(
    id               SERIAL PRIMARY KEY,
    amount           DECIMAL(19, 4),
    type             VARCHAR(50),
    status           VARCHAR(50),
    transaction_uuid VARCHAR(255) UNIQUE,
    comments         VARCHAR(255),
    created_at       TIMESTAMP,
    updated_by       VARCHAR(255),
    updated_at       TIMESTAMP,
    account_id       BIGINT REFERENCES accounts (id)
);
