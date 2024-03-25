-- CREATE TABLE passports
-- (
--     id           SERIAL PRIMARY KEY,
--     name         VARCHAR(255) NOT NULL,
--     surname      VARCHAR(255) NOT NULL,
--     birth_date   DATE,
--     personal_no  VARCHAR(255) NOT NULL UNIQUE,
--     expired_date DATE,
--     user_id      BIGINT,
--     FOREIGN KEY (user_id) REFERENCES users (id)
-- );
CREATE TABLE passports
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL CHECK (LENGTH(name) >= 3 AND LENGTH(name) <= 25),
    surname      VARCHAR(255) NOT NULL,
    birth_date   DATE         NOT NULL CHECK (birth_date <= CURRENT_DATE),
    personal_no  VARCHAR(7)   NOT NULL UNIQUE CHECK (LENGTH(personal_no) = 7),
    expired_date DATE         NOT NULL CHECK (expired_date > CURRENT_DATE)
);
