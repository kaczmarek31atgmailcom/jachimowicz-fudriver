-- MySQL 8.0, schema jachimowicz. Run the entire file in ONE session.
-- Recalculates ONLY existing zarobki records dated 2026-11-01..2026-11-30.
-- Closed periods only; breaks included. Cross-midnight periods are clipped
-- to each calendar day. Seconds are truncated to whole minutes.
-- One existing record per employee/day carries the total; the other records
-- carry zero (existing NULL values are preserved). All other columns stay intact.
-- Prefer a time-only row, matching DailyWorkTimeEarningsUpdater; if none exists,
-- use the lowest existing id. No rows are inserted or deleted, including paid rows.
-- This also recalculates godziny_pracy on paid records, without changing zaplacono.
-- Stop on any SQL error and execute ROLLBACK; do not use a continue-on-error client.

DROP TEMPORARY TABLE IF EXISTS tmp_work_minutes_2026_11;

START TRANSACTION;

-- Serialize with application work-time mutations before reading the periods.
SELECT p.id AS locked_person_id
FROM ludzie p
WHERE EXISTS (
    SELECT 1 FROM zarobki z
    WHERE z.ludzie_id = p.id
      AND z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01'
)
ORDER BY p.id
FOR UPDATE;

CREATE TEMPORARY TABLE tmp_work_minutes_2026_11 AS
SELECT targets.person_id, targets.work_day, targets.target_id,
       targets.old_minutes,
       COALESCE(TIMESTAMPDIFF(MINUTE,
           MIN(GREATEST(t.start_time, CAST(targets.work_day AS DATETIME))),
           MAX(LEAST(t.end_time, CAST(targets.work_day AS DATETIME) + INTERVAL 1 DAY))
       ), 0) AS new_minutes
FROM (
    SELECT z.ludzie_id AS person_id, DATE(z.`time`) AS work_day,
           COALESCE(MIN(CASE WHEN z.rodzaj_id IS NULL AND z.wozek_nr IS NULL
                                AND z.uniq_id IS NULL AND z.hala_id IS NULL
                                AND z.export IS NULL AND z.kraj IS NULL
                                AND z.inne IS NULL AND z.ilosc IS NULL
                                AND z.godziny_dodatkowe IS NULL
                             THEN z.id END), MIN(z.id)) AS target_id,
           SUM(COALESCE(z.godziny_pracy, 0)) AS old_minutes
    FROM zarobki z
    WHERE z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01'
    GROUP BY z.ludzie_id, DATE(z.`time`)
) targets
LEFT JOIN time_work_log t
    ON t.person_id = targets.person_id
   AND t.isOpened = 0
   AND t.end_time IS NOT NULL
   AND t.end_time >= t.start_time
   AND t.start_time < CAST(targets.work_day AS DATETIME) + INTERVAL 1 DAY
   AND t.end_time > CAST(targets.work_day AS DATETIME)
GROUP BY targets.person_id, targets.work_day, targets.target_id, targets.old_minutes;

-- Preview the old/new daily sums before the update.
SELECT person_id, work_day, target_id, old_minutes, new_minutes
FROM tmp_work_minutes_2026_11
ORDER BY work_day, person_id;

UPDATE zarobki z
JOIN tmp_work_minutes_2026_11 totals
  ON totals.person_id = z.ludzie_id AND totals.work_day = DATE(z.`time`)
SET z.godziny_pracy = CASE WHEN z.id = totals.target_id THEN totals.new_minutes ELSE 0 END
WHERE z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01'
  AND (z.id = totals.target_id OR z.godziny_pracy IS NOT NULL);

-- Verification: this result MUST be empty.
SELECT totals.person_id, totals.work_day, totals.new_minutes AS expected_minutes,
       SUM(COALESCE(z.godziny_pracy, 0)) AS actual_minutes
FROM tmp_work_minutes_2026_11 totals
JOIN zarobki z ON z.ludzie_id = totals.person_id AND DATE(z.`time`) = totals.work_day
GROUP BY totals.person_id, totals.work_day, totals.new_minutes
HAVING SUM(COALESCE(z.godziny_pracy, 0)) <> totals.new_minutes;

-- Informational: periods whose day has NO existing earnings record.
-- They are deliberately not inserted by this script.
WITH RECURSIVE days AS (
    SELECT DATE('2026-11-01') AS work_day
    UNION ALL
    SELECT work_day + INTERVAL 1 DAY FROM days WHERE work_day < '2026-11-30'
)
SELECT DISTINCT t.person_id, days.work_day AS day_without_earnings
FROM days
JOIN time_work_log t
  ON t.start_time < CAST(days.work_day AS DATETIME) + INTERVAL 1 DAY
 AND t.end_time > CAST(days.work_day AS DATETIME)
 AND t.end_time >= t.start_time AND t.isOpened = 0
WHERE NOT EXISTS (
    SELECT 1 FROM zarobki z
    WHERE z.ludzie_id = t.person_id
      AND z.`time` >= days.work_day AND z.`time` < days.work_day + INTERVAL 1 DAY
)
ORDER BY days.work_day, t.person_id;

COMMIT;
DROP TEMPORARY TABLE tmp_work_minutes_2026_11;
