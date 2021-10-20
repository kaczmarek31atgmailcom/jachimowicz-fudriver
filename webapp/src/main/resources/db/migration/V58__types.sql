ALTER TABLE rodzaj ADD uuid VARCHAR(60)
  AFTER id;
UPDATE rodzaj
SET uuid = id;

ALTER TABLE grupy_rodzaje ADD uuid VARCHAR(60)
  AFTER id;
UPDATE grupy_rodzaje
SET uuid = id;

ALTER TABLE skrz_rodzaj ADD uuid VARCHAR(60)
  AFTER id;
UPDATE skrz_rodzaj
SET uuid = id;

alter table skrz_rodzaj change name name VARCHAR(60) CHARACTER SET utf8mb4;
alter table grupy_rodzaje change name name varchar(60) CHARACTER SET utf8mb4;
alter table rodzaj change name name  varchar(60) CHARACTER SET utf8mb4;
