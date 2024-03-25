CREATE TABLE notifications
(
    id          SERIAL PRIMARY KEY,
    message     VARCHAR(255) NOT NULL,
    sent_date   TIMESTAMP    NOT NULL,
    read        BOOLEAN      NOT NULL,
    type        VARCHAR(50)  NOT NULL,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);
