-- MySQL / Percona 5.6+: generated columns are not available on these servers.
-- Run with work-time writes stopped. Existing active duplicates make the final
-- backfill fail; no work periods are silently closed or removed.
-- MySQL DDL is not transactional: inspect partial state before retrying a failure.
-- Conditional DDL allows retry after a failed backfill on nontransactional DDL.
SET @v86_sql = IF(
    EXISTS (SELECT 1 FROM information_schema.COLUMNS
            WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'time_work_log'
              AND COLUMN_NAME = 'active_person_id'),
    'SELECT 1',
    'ALTER TABLE time_work_log ADD COLUMN active_person_id INT NULL'
);
PREPARE v86_statement FROM @v86_sql;
EXECUTE v86_statement;
DEALLOCATE PREPARE v86_statement;

SET @v86_sql = IF(
    EXISTS (SELECT 1 FROM information_schema.STATISTICS
            WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'time_work_log'
              AND INDEX_NAME = 'time_work_log_unique_active_person'),
    'SELECT 1',
    'ALTER TABLE time_work_log ADD UNIQUE INDEX time_work_log_unique_active_person (active_person_id)'
);
PREPARE v86_statement FROM @v86_sql;
EXECUTE v86_statement;
DEALLOCATE PREPARE v86_statement;
SET @v86_sql = NULL;

DROP TRIGGER IF EXISTS time_work_log_active_person_insert;
DROP TRIGGER IF EXISTS time_work_log_active_person_update;

CREATE TRIGGER time_work_log_active_person_insert
BEFORE INSERT ON time_work_log
FOR EACH ROW
SET NEW.active_person_id = CASE WHEN NEW.isOpened = 1 THEN NEW.person_id ELSE NULL END;

CREATE TRIGGER time_work_log_active_person_update
BEFORE UPDATE ON time_work_log
FOR EACH ROW
SET NEW.active_person_id = CASE WHEN NEW.isOpened = 1 THEN NEW.person_id ELSE NULL END;

UPDATE time_work_log
SET active_person_id = CASE WHEN isOpened = 1 THEN person_id ELSE NULL END;
