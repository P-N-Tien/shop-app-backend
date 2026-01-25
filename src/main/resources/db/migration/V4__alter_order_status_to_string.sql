ALTER TABLE orders
    ADD COLUMN rename_status VARCHAR(20) DEFAULT '';

ALTER TABLE orders
DROP COLUMN status;

ALTER TABLE orders
    RENAME COLUMN rename_status TO status;