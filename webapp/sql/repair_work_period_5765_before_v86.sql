-- Percona/MySQL 5.6+. Aplikacja musi byc zatrzymana.
-- Nie zmienia zarobki ani innych okresow. Zachowuje oba rekordy.
-- Na podstawie potwierdzonych danych zamyka 5765 na poczatku 5764.
-- Domyslnie ROLLBACK. Po sprawdzeniu zmien koncowy ROLLBACK na COMMIT.
START TRANSACTION;
SELECT id, person_id, start_time, end_time, isOpened
FROM time_work_log WHERE person_id IN (2723,2877)
AND (start_time >= '2026-11-19' OR isOpened=1)
ORDER BY person_id,start_time,id FOR UPDATE;

UPDATE time_work_log old_period
JOIN time_work_log next_period ON next_period.id=5764
SET old_period.end_time=next_period.start_time, old_period.isOpened=0
WHERE old_period.id=5765 AND old_period.person_id=2723
  AND old_period.start_time='2026-11-19 07:01:07'
  AND old_period.isOpened=1 AND old_period.end_time IS NULL
  AND next_period.person_id=2723 AND next_period.isOpened=0
  AND next_period.start_time='2026-11-19 07:01:12'
  AND next_period.end_time='2026-11-19 13:07:22';
SELECT ROW_COUNT() AS corrected_periods;

-- Oczekiwane: 5765 zakonczony o 07:01:12; 5903 pozostaje otwarty.
SELECT id,person_id,start_time,end_time,isOpened,active_person_id
FROM time_work_log WHERE id IN (5765,5764,5903) ORDER BY id;

-- Wynik musi byc pusty przed ponowieniem V86.
SELECT person_id, COUNT(*) AS active_periods, GROUP_CONCAT(id ORDER BY id) AS ids
FROM time_work_log WHERE isOpened=1 GROUP BY person_id HAVING COUNT(*)>1;
ROLLBACK;
