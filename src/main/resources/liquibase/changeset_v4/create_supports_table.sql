CREATE TABLE supports
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    phone_number VARCHAR(20)  NOT NULL,
    email        VARCHAR(255) NOT NULL,
    message      TEXT         NOT NULL,
    is_answered  BOOLEAN DEFAULT false
);