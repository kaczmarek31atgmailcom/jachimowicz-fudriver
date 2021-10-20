ALTER TABLE skrz_cust ADD uuid VARCHAR(60) AFTER id;
ALTER TABLE skrz_cust change name name VARCHAR(100) CHARACTER SET utf8mb4;
ALTER TABLE skrz_cust ADD address VARCHAR(100) CHARACTER SET utf8mb4 AFTER name;
UPDATE skrz_cust SET uuid = id;