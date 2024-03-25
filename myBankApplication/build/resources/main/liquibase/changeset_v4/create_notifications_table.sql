-- CREATE TABLE notifications
-- (
--     id        SERIAL PRIMARY KEY,
--     message   VARCHAR(255) NOT NULL,
--     sent_date TIMESTAMP    NOT NULL,
--     read      BOOLEAN      NOT NULL,
--     type      VARCHAR(50)  NOT NULL,
--     user_id   BIGINT,
--     FOREIGN KEY (user_id) REFERENCES users (id)
-- );
CREATE TABLE notifications
(
    id        SERIAL PRIMARY KEY,
    message   TEXT,
    is_read   BOOLEAN,
    sent_date TIMESTAMP,
    type      VARCHAR(255),
    user_id   BIGINT REFERENCES users (id)
);
