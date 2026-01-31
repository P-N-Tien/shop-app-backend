ALTER TABLE product_images
    ADD COLUMN sort_order INT DEFAULT 0;

ALTER TABLE product_images
    ADD COLUMN is_primary BOOLEAN DEFAULT FALSE;