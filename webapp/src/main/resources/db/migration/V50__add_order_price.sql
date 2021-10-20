ALTER TABLE batch ADD price INT AFTER status;
ALTER TABLE batch ADD currency varchar(10) CHARACTER SET utf8mb4 AFTER price;
ALTER TABLE wz_brc_detail ADD price INT;
ALTER TABLE wz_brc_detail ADD currency VARCHAR(6);