CREATE TABLE passports
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    surname      VARCHAR(255) NOT NULL,
    birth_date   DATE,
    personal_no  VARCHAR(255) NOT NULL UNIQUE,
    expired_date DATE,
    customer_id  BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);
