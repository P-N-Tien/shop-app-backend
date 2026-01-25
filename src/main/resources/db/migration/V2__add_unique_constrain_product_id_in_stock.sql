ALTER TABLE stocks
    ADD CONSTRAINT uq_stock_product UNIQUE (product_id);