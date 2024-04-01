CREATE TABLE notifications
(
    id        SERIAL PRIMARY KEY,
    message   TEXT,
    sent_date TIMESTAMP,
    type      VARCHAR(255),
    user_id   BIGINT REFERENCES users (id)
);
