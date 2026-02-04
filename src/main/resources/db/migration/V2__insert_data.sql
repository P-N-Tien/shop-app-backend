INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO categories (name)
VALUES ('Camera'),
       ('Clothes'),
       ('Gaming'),
       ('Computers');

INSERT INTO users (id, full_name, phone_number, password, date_of_birth, address, status)
VALUES (1, 'Pham Ngoc Tien', '00000000001',
        '$2a$10$.tJH8FHAXcK883XC60JNn.DywnSPpkkc/3fNxy/osL7G2SZpBKJWC',
        '1995-05-15', '123 Le Loi Street, TP.HCM', 'ACTIVE'),
       (2, 'Elon Musk', '00000000002',
        '$2a$10$.tJH8FHAXcK883XC60JNn.DywnSPpkkc/3fNxy/osL7G2SZpBKJWC',
        '1998-10-20', '456 Le Thanh Ton Street, Quang Ngai', 'ACTIVE');

INSERT INTO user_roles (role_id, user_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO products (name, price, status, thumbnail_url, description, category_id)
VALUES ('CANON EOS DSLR CAMERA', 35000000.00, 'ACTIVE',
        'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770045403/products/1/dv4idhqhvjk8b55rdm2d.webp',
        'Capture life''s precious moments with the CANON EOS DSLR Camera. Whether you''re a professional photographer or an amateur enthusiast, this camera delivers stunning image quality and performance. With its advanced features and intuitive controls, you can unleash your creativity and take your photography to the next level. Say goodbye to blurry shots and hello to crystal-clear images with the CANON EOS DSLR Camera.',
        1),
       ('Lenovo RTX 3050', 28000000.00, 'ACTIVE',
        'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770047827/products/2/uwtyislqttu4hurorruj.webp',
        'Experience unparalleled gaming performance with the ASUS FHD Gaming Laptop. Powered by cutting-edge hardware and featuring a stunning Full HD display, this laptop is built to handle even the most demanding games with ease. Its sleek design and lightweight construction make it the perfect companion for gaming on the go. Say goodbye to lag and hello to smooth gameplay with the ASUS FHD Gaming Laptop.',
        4),
       ('North Coat', 5000000.00, 'ACTIVE',
        'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051768/products/3/nqyjk8weeu2hbkamhcxn.webp',
        'Stay warm and stylish with The North Coat. Made from premium materials and expert craftsmanship, this coat is designed to withstand the elements while keeping you cozy. Its timeless design and versatile color make it a wardrobe essential for any season. Whether you''re braving the outdoors or running errands in the city, The North Coat is sure to turn heads wherever you go.',
        2),
       ('PS5 GAMEPAD', 10900000.00, 'ACTIVE',
        'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051700/products/4/cokkek5nnhzxwa9uxn7l.webp',
        'High-quality PlayStation 5 Controller Skin with air channel adhesive for easy bubble-free installation and mess-free removal. Pressure-sensitive.',
        3);

INSERT INTO product_images (id, image_url, product_id, sort_order, is_primary)
VALUES (1, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770045402/products/1/oqc4n9oxr7dhis5bg02i.webp', 1, 0,
        FALSE),
       (2, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770045403/products/1/dv4idhqhvjk8b55rdm2d.webp', 1, 1,
        TRUE),
       (3, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770045406/products/1/lxz1fwsf0zytmgihnqaw.webp', 1, 2,
        FALSE),
       (4, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770045412/products/1/lkzkd3xmvcq1e6uvcbzj.webp', 1, 3,
        FALSE),
       (5, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770047833/products/2/tnijttdqtfwmztigcwb1.webp', 2, 0,
        TRUE),
       (6, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770047824/products/2/f3httipzunbmuigklo41.webp', 2, 2,
        FALSE),
       (7, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770047827/products/2/uwtyislqttu4hurorruj.webp', 2, 1,
        FALSE),
       (8, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770047831/products/2/giczpbdrkkjn7dnk00e3.webp', 2, 3,
        FALSE),
       (9, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051697/products/4/bn48nolt854o03ftort3.webp', 4, 0,
        FALSE),
       (10, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051699/products/4/m9dqf2gzwocab5tjldkx.webp', 4, 1,
        FALSE),
       (11, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051700/products/4/cokkek5nnhzxwa9uxn7l.webp', 4, 2,
        TRUE),
       (12, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051701/products/4/vfmo7yclctc7xoyjoc17.webp', 4, 3,
        FALSE),
       (13, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051703/products/4/gkm5q8v2z4hkbnsjaczq.webp', 4, 4,
        FALSE),
       (14, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051766/products/3/unxcc5wwq9wyzujuofn8.webp', 3, 1,
        FALSE),
       (15, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051770/products/3/kcxbx9dvbfq8fvtyukmd.webp', 3, 3,
        FALSE),
       (16, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051768/products/3/nqyjk8weeu2hbkamhcxn.webp', 3, 2,
        FALSE),
       (17, 'https://res.cloudinary.com/dwrco1qc5/image/upload/v1770051765/products/3/xtyydzer5sg95dpbsxku.webp', 3, 0,
        TRUE);


INSERT INTO inventories (quantity, reserved_quantity, sold_quantity, version, product_id)
VALUES (100, 0, 0, 1, 1),
       (100, 0, 0, 1, 2),
       (100, 0, 0, 1, 3),
       (100, 0, 0, 1, 4);

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
