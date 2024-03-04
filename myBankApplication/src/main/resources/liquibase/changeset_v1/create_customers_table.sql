-- CREATE SEQUENCE cif_sequence START 1000;

CREATE TABLE customers
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    birth_date   DATE,
    email        VARCHAR(255) NOT NULL UNIQUE,
--     cif VARCHAR(4) DEFAULT CONCAT(nextval('cif_sequence')),
    phone_number VARCHAR(255)

--     CONSTRAINT check_cif_length CHECK (LENGTH(cif) = 7)
);
