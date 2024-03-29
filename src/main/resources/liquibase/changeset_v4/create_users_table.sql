CREATE TABLE users
(
    id              SERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    cif             VARCHAR(5),
    role            VARCHAR(50)  NOT NULL,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(255),
    updated_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    user_profile_id INT,
    CONSTRAINT fk_user_profile_id
        FOREIGN KEY (user_profile_id)
            REFERENCES user_profile (user_profile_id)
);

-- Optionally, you can create an index on the email column for faster lookups
CREATE UNIQUE INDEX idx_users_email ON users (email);

