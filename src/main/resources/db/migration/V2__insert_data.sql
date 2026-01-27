INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO categories (name)
VALUES ('Smart phone'),
       ('Laptop'),
       ('Apple watch'),
       ('Air pod');

INSERT INTO users (id, full_name, phone_number, password, date_of_birth, address, status)
VALUES (1, 'Pham Ngoc Tien', '0901234567',
        '$2a$10$.tJH8FHAXcK883XC60JNn.DywnSPpkkc/3fNxy/osL7G2SZpBKJWC',
        '1995-05-15', '123 Le Loi Street, TP.HCM', 'ACTIVE'),
       (2, 'Khoa Pug', '0909876543',
        '$2a$10$.tJH8FHAXcK883XC60JNn.DywnSPpkkc/3fNxy/osL7G2SZpBKJWC',
        '1998-10-20', '456 Le Thanh Ton Street, Quang Ngai', 'ACTIVE');

INSERT INTO user_roles (role_id, user_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO products (name, price, status, description, category_id)
VALUES ('iPhone 17 Pro Max', 35000000.00, 'ACTIVE', '1TB', 1),
       ('Macbook M2 Pro', 28000000.00, 'ACTIVE', 'RAM: 16GB, Disk: 512G', 2);

INSERT INTO product_images (id, image_url, product_id)
VALUES (1, 'https://cdn.example.com/iphone17-front.png', 1),
       (2, 'https://cdn.example.com/ipad-m4.png', 2);

INSERT INTO inventories (quantity, reserved_quantity, sold_quantity, version, product_id)
VALUES (100, 0, 0, 1, 1),
       (50, 0, 0, 1, 2);


INSERT INTO orders (total_money, recipient_name, recipient_phone,
                    recipient_address, status, payment_method, user_id)
VALUES (35000000.00, 'Elon Musk', '0909876543',
        '456 Nguyen Hue Street', 'PAID', 'VNPAY', 2);

INSERT INTO order_details (name, quantity, price_at_purchase, order_id, product_id)
VALUES ('iPhone 17 Pro Max', 1, 35000000.00, 1, 1);

INSERT INTO payments (transaction_id, method, status, amount, bank_code,
                      card_type, response_code, failure_reason, payment_time, order_id)
VALUES ('TXN20260126-001', 'VNPAY', 'SUCCESS', 35000000.00,
        'NCB', 'ATM', '00', 'NONE', '2026-01-26 10:00:00', 1);
