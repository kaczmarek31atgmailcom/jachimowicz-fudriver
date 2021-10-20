ALTER TABLE local_reclassification_log ADD uuid VARCHAR(60)
AFTER id;
ALTER TABLE local_reclassification_log ADD warehouse_only TINYINT;