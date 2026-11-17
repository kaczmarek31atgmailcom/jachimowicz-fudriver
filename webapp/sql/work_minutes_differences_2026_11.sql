-- MySQL 8.0. Read-only comparison for November 2026.
-- Compare DAILY sums: earnings can contain multiple rows for an employee/day.
-- Expected minutes include breaks between closed periods; open periods are ignored.
-- Periods crossing midnight are clipped to each calendar day.
-- Also includes closed periods without an earnings record and earnings without
-- closed periods. Only differing totals are returned.
WITH RECURSIVE days AS (
    SELECT DATE('2026-11-01') AS work_day
    UNION ALL
    SELECT work_day + INTERVAL 1 DAY FROM days WHERE work_day < '2026-11-30'
), expected AS (
    SELECT t.person_id, d.work_day,
           TIMESTAMPDIFF(MINUTE,
               MIN(GREATEST(t.start_time, CAST(d.work_day AS DATETIME))),
               MAX(LEAST(t.end_time, CAST(d.work_day AS DATETIME) + INTERVAL 1 DAY))
           ) AS expected_minutes,
           MIN(GREATEST(t.start_time, CAST(d.work_day AS DATETIME))) AS first_start,
           MAX(LEAST(t.end_time, CAST(d.work_day AS DATETIME) + INTERVAL 1 DAY)) AS last_end,
           GROUP_CONCAT(t.id ORDER BY t.start_time, t.id) AS work_period_ids
    FROM days d
    JOIN time_work_log t
      ON t.start_time < CAST(d.work_day AS DATETIME) + INTERVAL 1 DAY
     AND t.end_time > CAST(d.work_day AS DATETIME)
     AND t.end_time >= t.start_time
     AND t.isOpened = 0
    GROUP BY t.person_id, d.work_day
), recorded AS (
    SELECT z.ludzie_id AS person_id, DATE(z.`time`) AS work_day,
           SUM(COALESCE(z.godziny_pracy, 0)) AS recorded_minutes,
           GROUP_CONCAT(z.id ORDER BY z.id) AS earnings_ids
    FROM zarobki z
    WHERE z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01'
    GROUP BY z.ludzie_id, DATE(z.`time`)
), employee_days AS (
    SELECT person_id, work_day FROM expected
    UNION
    SELECT person_id, work_day FROM recorded
)
SELECT k.person_id, p.imie, p.nazwisko, k.work_day,
       COALESCE(r.recorded_minutes, 0) AS recorded_minutes,
       COALESCE(e.expected_minutes, 0) AS expected_minutes,
       COALESCE(e.expected_minutes, 0) - COALESCE(r.recorded_minutes, 0) AS difference_minutes,
       e.first_start, e.last_end, r.earnings_ids, e.work_period_ids,
       CASE WHEN r.person_id IS NULL THEN 'BRAK_REKORDU_ZAROBKI'
            WHEN e.person_id IS NULL THEN 'BRAK_ZAMKNIETYCH_OKRESOW'
            ELSE 'ROZNA_SUMA_MINUT' END AS reason
FROM employee_days k
LEFT JOIN ludzie p ON p.id = k.person_id
LEFT JOIN expected e ON e.person_id = k.person_id AND e.work_day = k.work_day
LEFT JOIN recorded r ON r.person_id = k.person_id AND r.work_day = k.work_day
WHERE COALESCE(e.expected_minutes, 0) <> COALESCE(r.recorded_minutes, 0)
ORDER BY k.work_day, k.person_id;
