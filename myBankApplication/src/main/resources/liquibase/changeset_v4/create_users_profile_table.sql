CREATE TABLE user_profile
(
    user_profile_id SERIAL PRIMARY KEY,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    birth_date      DATE         NOT NULL,
    phone_number    VARCHAR(20)  NOT NULL,
    gender          VARCHAR(10)  NOT NULL,
    nationality     VARCHAR(50)  NOT NULL,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(255),
    updated_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255)
);
