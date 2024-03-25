-- CREATE TABLE transactions
-- (
--     id               SERIAL PRIMARY KEY,
--     description      VARCHAR(255),
--     amount           NUMERIC(38, 2),
--     transaction_date TIMESTAMP,
--     type             VARCHAR(255),
--     status           VARCHAR(255),
--     operator_id      BIGINT,
--     transaction_uuid VARCHAR(255) UNIQUE,
--     account_id       BIGINT,
--     FOREIGN KEY (account_id) REFERENCES accounts (id)
-- );
CREATE TABLE transactions
(
    id                SERIAL PRIMARY KEY,
    amount            DECIMAL(19, 4),
    type              VARCHAR(50),
    status            VARCHAR(50),
    transaction_uuid  VARCHAR(255) UNIQUE,
    comments       VARCHAR(255),
    created_at        TIMESTAMP,
    updated_by        VARCHAR(255),
    updated_at        TIMESTAMP,
    account_id        BIGINT REFERENCES accounts (id)
);
