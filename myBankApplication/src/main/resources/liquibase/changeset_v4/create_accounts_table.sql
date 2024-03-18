-- CREATE TABLE accounts
-- (
--     id                  SERIAL PRIMARY KEY,
--     branch_code         VARCHAR(255),
--     account_number      VARCHAR(255) UNIQUE,
--     account_open_date   TIMESTAMP,
--     account_expire_date TIMESTAMP,
--     currency_type       VARCHAR(255),
--     account_type        VARCHAR(255),
--     status              VARCHAR(255),
--     available_balance   NUMERIC(38, 2),
--     current_balance     NUMERIC(38, 2),
--     pin                 VARCHAR(255),
--     user_id             BIGINT,
--     FOREIGN KEY (user_id) REFERENCES users (id)
-- );

-- customer_id         bigint REFERENCES customers (id),

-- CREATE TABLE accounts
-- (
--     id                  SERIAL PRIMARY KEY,
--     branch_code         VARCHAR(255),
--     account_number      VARCHAR(255)   NOT NULL UNIQUE,
--     account_open_date   TIMESTAMP      NOT NULL,
--     account_expire_date DATE           NOT NULL,
--     currency_type       VARCHAR(255)   NOT NULL,
--     account_type        VARCHAR(255)   NOT NULL,
--     status              VARCHAR(255) DEFAULT 'Active',
--     available_balance   DECIMAL(38, 2) NOT NULL,
--     current_balance     DECIMAL(38, 2) NOT NULL,
--     pin                 VARCHAR(255),
--     created_by          VARCHAR(255),
--     updated_at          TIMESTAMP,
--     user_id             BIGINT REFERENCES users (id)
-- );
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
    created_at          TIMESTAMP         NOT NULL,
    created_by          VARCHAR(255)      NOT NULL,
    updated_at          TIMESTAMP         NOT NULL,
    updated_by          VARCHAR(255),
    user_id             BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

