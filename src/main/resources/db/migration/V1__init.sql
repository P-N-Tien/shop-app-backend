
CREATE TABLE IF NOT EXISTS categories(
    id SERIAL2 PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    price BIGINT CHECK ( price >= 0 ),
    description VARCHAR(200),
    thumbnail VARCHAR,
    status BOOLEAN DEFAULT TRUE,
    category_id SMALLINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- V2__create_product_index.sql
-- CREATE INDEX idx_products_category ON products (category_id);
-- CREATE INDEX idx_products_price ON products (price);



