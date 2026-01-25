CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(50) NOT NULL,
    gateway VARCHAR(50) NOT NULL,
    bank_code VARCHAR(50),
    card_type VARCHAR(50),
    failure_reason VARCHAR(255),
    response_code VARCHAR(10),
    payment_time TIMESTAMP WITHOUT TIME ZONE,
    metadata TEXT,
    order_id BIGINT UNIQUE,

    FOREIGN KEY (order_id) REFERENCES orders (id)
);
