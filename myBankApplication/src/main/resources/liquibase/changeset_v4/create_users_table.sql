CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    birth_date   DATE         NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    cif          VARCHAR(5),
    role         VARCHAR(255) NOT NULL
);
