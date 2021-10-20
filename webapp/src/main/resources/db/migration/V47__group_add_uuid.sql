ALTER TABLE grupy ADD uuid VARCHAR(60)
  AFTER id;
UPDATE grupy
SET uuid = id;
