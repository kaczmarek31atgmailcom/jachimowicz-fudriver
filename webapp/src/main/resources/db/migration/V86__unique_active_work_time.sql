-- Existing active duplicates must be reviewed and resolved before this migration.
-- Keep the column and index in one ALTER: duplicate rows make the operation fail
-- without silently closing or deleting any work periods.
ALTER TABLE time_work_log
    ADD COLUMN active_person_id INT GENERATED ALWAYS AS
        (CASE WHEN isOpened = 1 THEN person_id ELSE NULL END) STORED,
    ADD UNIQUE INDEX time_work_log_unique_active_person (active_person_id);
