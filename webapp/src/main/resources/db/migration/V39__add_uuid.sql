ALTER TABLE warehouse_brc ADD uuid VARCHAR(60)
AFTER id;
UPDATE warehouse_brc
SET uuid = id;

ALTER TABLE batch ADD uuid VARCHAR(60)
AFTER id;
UPDATE batch
SET uuid = id;

ALTER TABLE orders ADD uuid VARCHAR(60)
AFTER id;
UPDATE orders
SET uuid = id;

ALTER TABLE palette ADD uuid VARCHAR(60)
AFTER id;
UPDATE palette set uuid = id;

ALTER TABLE wz_brc_header ADD uuid VARCHAR(60)
AFTER id;
UPDATE wz_brc_header
SET uuid = id;

ALTER TABLE wz_brc_detail ADD uuid VARCHAR(60)
AFTER id;
UPDATE wz_brc_detail
SET uuid = id;

ALTER TABLE wz_brc_palette ADD uuid VARCHAR(60)
AFTER id;
UPDATE wz_brc_palette
SET uuid = id;

