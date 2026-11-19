-- MySQL / Percona 5.6.22+ / InnoDB. Bez CTE i funkcji okienkowych. Uruchom CALY plik w jednej sesji w bazie aplikacji.
-- Najpierw scalenie zamknietych okresow, potem przeliczenie istniejacych zarobki.
-- Kazda spojna grupa NAKLADAJACYCH sie okresow staje sie jednym okresem:
-- MIN(start_time)..MAX(end_time). Zachowujemy najmniejsze id, usuwamy reszte.
-- Grupy lancuchowe i zawieranie sa obslugiwane; stykajace sie okresy nie sa scalane.
-- Zachowuje to sume zajetego czasu, NIE odtwarza rzeczywistej godziny wyjscia.
-- Konce automatyczne 23:59 pozostaja uwzglednione.
-- Pomijamy grupy dotykajace dni oplaconych, okresow otwartych albo granic miesiaca.
-- Pominięcia sa raportowane. Okresy niepoprawne (end<=start) nie sa scalane.
-- Zarobki: tylko istniejace wpisy z listopada; bez tworzenia brakujacych dni.
-- czy_akord=1: rozpiętosc dnia minus 30 min gdy >210; czy_akord=0:
-- suma okresow bez przerw +15 min raz przy >=3 okresach PO SCALENIU.
-- godziny_pracy to MINUTY. Czasy przez polnoc sa dzielone na dni.
-- Oplacone dni i dni z pozostalymi nakladaniami nie sa przeliczane.
-- Przed uzyciem wykonaj kopie bazy; zatrzymaj zapisy RFID, edycje i rozliczenia.
-- Wymagany brak zewnetrznych FK do time_work_log.id (sprawdz raport ponizej).
-- Nie uzywaj klienta kontynuujacego po bledzie; po bledzie wykonaj ROLLBACK.
-- DOMYSLNIE ROLLBACK. Po ocenie raportow zamien ostatni ROLLBACK na COMMIT.
-- Ponowne uruchomienie nie powoduje kolejnego scalania poprawionych grup.

-- Wynik powinien byc pusty. Jesli sa zaleznosci FK, przerwij przed START TRANSACTION.
SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA=DATABASE() AND REFERENCED_TABLE_NAME='time_work_log';

DROP TEMPORARY TABLE IF EXISTS tmp_merge_members_nov2026;
DROP TEMPORARY TABLE IF EXISTS tmp_merge_groups_nov2026;
DROP TEMPORARY TABLE IF EXISTS tmp_payroll_nov2026;
DROP TEMPORARY TABLE IF EXISTS tmp56_logs;
DROP TEMPORARY TABLE IF EXISTS tmp56_previous;
DROP TEMPORARY TABLE IF EXISTS tmp56_marked;
DROP TEMPORARY TABLE IF EXISTS tmp56_anchors;
DROP TEMPORARY TABLE IF EXISTS tmp56_grouped;
DROP TEMPORARY TABLE IF EXISTS tmp56_targets;
DROP TEMPORARY TABLE IF EXISTS tmp56_periods;
DROP TEMPORARY TABLE IF EXISTS tmp56_periods_copy;
DROP TEMPORARY TABLE IF EXISTS tmp56_ordered_periods;
DROP TEMPORARY TABLE IF EXISTS tmp56_totals;
START TRANSACTION;

SELECT id AS locked_person_id FROM ludzie ORDER BY id FOR UPDATE;
SELECT id AS locked_period_id FROM time_work_log
WHERE start_time<'2026-12-01' AND (end_time>'2026-11-01' OR isOpened=1)
ORDER BY person_id,id FOR UPDATE;
SELECT id AS locked_earnings_id FROM zarobki
WHERE `time`>='2026-11-01' AND `time`<'2026-12-01'
ORDER BY ludzie_id,id FOR UPDATE;

CREATE TEMPORARY TABLE tmp56_logs (INDEX employee_start (person_id,start_time,id)) ENGINE=InnoDB AS
SELECT id,person_id,start_time,end_time FROM time_work_log
WHERE isOpened=0 AND end_time>start_time
AND start_time<'2026-12-01' AND end_time>'2026-11-01';
-- Osobna kopia: MySQL 5.6 nie pozwala dwukrotnie otwierac tej samej tabeli tymczasowej.
CREATE TEMPORARY TABLE tmp56_previous (INDEX employee_start (person_id,start_time,id)) ENGINE=InnoDB AS SELECT * FROM tmp56_logs;

CREATE TEMPORARY TABLE tmp56_marked ENGINE=InnoDB AS
SELECT t.id,t.person_id,t.start_time,t.end_time,
 CASE WHEN t.start_time<MAX(p.end_time) THEN 0 ELSE 1 END AS new_group
FROM tmp56_logs t LEFT JOIN tmp56_previous p ON p.person_id=t.person_id
 AND (p.start_time<t.start_time OR (p.start_time=t.start_time AND p.id<t.id))
GROUP BY t.id,t.person_id,t.start_time,t.end_time;
CREATE TEMPORARY TABLE tmp56_anchors (INDEX employee_start (person_id,start_time,id)) ENGINE=InnoDB AS
SELECT id,person_id,start_time FROM tmp56_marked WHERE new_group=1;

CREATE TEMPORARY TABLE tmp_merge_members_nov2026 ENGINE=InnoDB AS
SELECT t.id,t.person_id,t.start_time,t.end_time,COUNT(a.id) AS group_number
FROM tmp56_marked t JOIN tmp56_anchors a ON a.person_id=t.person_id
 AND (a.start_time<t.start_time OR (a.start_time=t.start_time AND a.id<=t.id))
GROUP BY t.id,t.person_id,t.start_time,t.end_time;

CREATE TEMPORARY TABLE tmp56_grouped ENGINE=InnoDB AS
SELECT person_id,group_number,MIN(id) AS keep_id,COUNT(*) AS member_count,
 MIN(start_time) AS merged_start,MAX(end_time) AS merged_end
FROM tmp_merge_members_nov2026 GROUP BY person_id,group_number HAVING COUNT(*)>1;
CREATE TEMPORARY TABLE tmp_merge_groups_nov2026 ENGINE=InnoDB AS
SELECT g.*,
 CASE WHEN merged_start<'2026-11-01' OR merged_end>'2026-12-01'
 THEN 'POMINIETO_GRANICE_MIESIACA'
 WHEN EXISTS (SELECT 1 FROM zarobki z WHERE z.ludzie_id=g.person_id AND z.zaplacono=1
 AND z.`time`>=DATE(g.merged_start)
 AND z.`time`<DATE(g.merged_end-INTERVAL 1 MICROSECOND)+INTERVAL 1 DAY)
 THEN 'POMINIETO_OPLACONY_DZIEN'
 WHEN EXISTS (SELECT 1 FROM time_work_log t WHERE t.person_id=g.person_id
 AND t.isOpened=1 AND t.start_time<g.merged_end)
 THEN 'POMINIETO_OTWARTY_OKRES'
 ELSE 'DO_SCALENIA' END AS status
FROM tmp56_grouped g;

-- Dokladny plan: oryginalne okresy, zachowywane id, nowe granice i status.
SELECT m.*,g.keep_id,g.merged_start,g.merged_end,g.status
FROM tmp_merge_members_nov2026 m JOIN tmp_merge_groups_nov2026 g
 ON g.person_id=m.person_id AND g.group_number=m.group_number
ORDER BY m.person_id,m.start_time,m.id;
SELECT status,COUNT(*) AS groups_count,SUM(member_count-1) AS redundant_rows
FROM tmp_merge_groups_nov2026 GROUP BY status;

UPDATE time_work_log t JOIN tmp_merge_groups_nov2026 g ON t.id=g.keep_id
SET t.start_time=g.merged_start,t.end_time=g.merged_end
WHERE g.status='DO_SCALENIA';

DELETE t FROM time_work_log t
JOIN tmp_merge_members_nov2026 m ON m.id=t.id
JOIN tmp_merge_groups_nov2026 g ON g.person_id=m.person_id AND g.group_number=m.group_number
WHERE g.status='DO_SCALENIA' AND t.id<>g.keep_id;

-- Przeliczanie zarobki po scaleniach. Raport pokazuje tez pominięte dni.
CREATE TEMPORARY TABLE tmp56_targets ENGINE=InnoDB AS
    SELECT z.ludzie_id AS person_id, DATE(z.`time`) AS work_day,
           COALESCE(MIN(CASE WHEN z.rodzaj_id IS NULL AND z.wozek_nr IS NULL
                AND z.uniq_id IS NULL AND z.hala_id IS NULL
                AND z.export IS NULL AND z.kraj IS NULL AND z.inne IS NULL
                AND z.ilosc IS NULL AND z.godziny_dodatkowe IS NULL
                THEN z.id END), MIN(z.id)) AS target_id,
           SUM(COALESCE(z.godziny_pracy,0)) AS old_minutes,
           MAX(CASE WHEN z.zaplacono=1 THEN 1 ELSE 0 END) AS paid
    FROM zarobki z
    WHERE z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01'
    GROUP BY z.ludzie_id, DATE(z.`time`)
;
CREATE TEMPORARY TABLE tmp56_periods ENGINE=InnoDB AS
    SELECT k.person_id, k.work_day, t.id,
           GREATEST(t.start_time, CAST(k.work_day AS DATETIME)) AS period_start,
           LEAST(t.end_time, CAST(k.work_day AS DATETIME)+INTERVAL 1 DAY) AS period_end
    FROM tmp56_targets k JOIN time_work_log t ON t.person_id=k.person_id
      AND t.isOpened=0 AND t.end_time > t.start_time
      AND t.start_time < CAST(k.work_day AS DATETIME)+INTERVAL 1 DAY
      AND t.end_time > CAST(k.work_day AS DATETIME)
;
CREATE TEMPORARY TABLE tmp56_periods_copy (INDEX employee_day_start (person_id,work_day,period_start,id)) ENGINE=InnoDB AS SELECT * FROM tmp56_periods;
CREATE TEMPORARY TABLE tmp56_ordered_periods ENGINE=InnoDB AS
SELECT t.person_id,t.work_day,t.id,t.period_start,t.period_end,MAX(p.period_end) AS previous_end
FROM tmp56_periods t LEFT JOIN tmp56_periods_copy p
 ON p.person_id=t.person_id AND p.work_day=t.work_day
 AND (p.period_start<t.period_start OR (p.period_start=t.period_start AND p.id<t.id))
GROUP BY t.person_id,t.work_day,t.id,t.period_start,t.period_end;
CREATE TEMPORARY TABLE tmp56_totals ENGINE=InnoDB AS
    SELECT person_id, work_day, COUNT(*) AS period_count,
           TIMESTAMPDIFF(MINUTE, MIN(period_start), MAX(period_end)) AS span_minutes,
           FLOOR(SUM(TIMESTAMPDIFF(MICROSECOND, period_start, period_end))/60000000) AS worked_minutes,
           MAX(CASE WHEN period_start < previous_end THEN 1 ELSE 0 END) AS overlaps
    FROM tmp56_ordered_periods GROUP BY person_id, work_day;
CREATE TEMPORARY TABLE tmp_payroll_nov2026 ENGINE=InnoDB AS
SELECT k.*, p.czy_akord,
       COALESCE(t.period_count,0) AS period_count,
       CASE WHEN p.czy_akord=1 THEN COALESCE(t.span_minutes,0)
                   - CASE WHEN t.span_minutes > 210 THEN 30 ELSE 0 END
            ELSE COALESCE(t.worked_minutes,0)
                   + CASE WHEN t.period_count >= 3 THEN 15 ELSE 0 END END AS new_minutes,
       CASE WHEN k.paid=1 THEN 'POMINIETO_OPLACONY_DZIEN'
            WHEN p.czy_akord IS NULL OR p.czy_akord NOT IN (0,1) THEN 'POMINIETO_TYP_PRACOWNIKA'
            WHEN t.overlaps=1 THEN 'POMINIETO_NAKLADAJACE_OKRESY'
            ELSE 'DO_PRZELICZENIA' END AS status
FROM tmp56_targets k LEFT JOIN ludzie p ON p.id=k.person_id
LEFT JOIN tmp56_totals t ON t.person_id=k.person_id AND t.work_day=k.work_day;

-- Podglad wszystkich dni, w tym pominietych. new_minutes ma znaczenie tylko
-- dla statusu DO_PRZELICZENIA; dla pominietych dni nie jest zapisywane.
SELECT person_id, work_day, czy_akord, target_id, period_count,
       old_minutes, new_minutes, status
FROM tmp_payroll_nov2026 ORDER BY work_day, person_id;

UPDATE zarobki z JOIN tmp_payroll_nov2026 t
  ON t.person_id=z.ludzie_id AND z.`time` >= t.work_day
 AND z.`time` < t.work_day+INTERVAL 1 DAY
SET z.godziny_pracy=CASE WHEN z.id=t.target_id THEN t.new_minutes ELSE 0 END,
    z.timeshort=202611
WHERE t.status='DO_PRZELICZENIA' AND z.zaplacono=0
  AND z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01';

-- Kontrola po UPDATE: wynik musi byc pusty.
SELECT t.person_id, t.work_day, t.new_minutes AS expected_minutes,
       SUM(COALESCE(z.godziny_pracy,0)) AS actual_minutes
FROM tmp_payroll_nov2026 t JOIN zarobki z ON z.ludzie_id=t.person_id
 AND z.`time` >= t.work_day AND z.`time` < t.work_day+INTERVAL 1 DAY
WHERE t.status='DO_PRZELICZENIA'
GROUP BY t.person_id, t.work_day, t.new_minutes
HAVING actual_minutes <> expected_minutes;

SELECT status, COUNT(*) AS employee_days FROM tmp_payroll_nov2026 GROUP BY status;

-- Pozostale konflikty (np. na oplaconych dniach): raport do recznej oceny.
SELECT a.person_id,a.id AS first_id,b.id AS second_id
FROM time_work_log a JOIN time_work_log b ON a.person_id=b.person_id AND a.id<b.id
WHERE a.isOpened=0 AND b.isOpened=0 AND a.end_time>a.start_time AND b.end_time>b.start_time
AND a.start_time<b.end_time AND b.start_time<a.end_time
AND GREATEST(a.start_time,b.start_time)<'2026-12-01'
AND LEAST(a.end_time,b.end_time)>'2026-11-01'
ORDER BY a.person_id,a.id,b.id;

ROLLBACK;
DROP TEMPORARY TABLE tmp_payroll_nov2026;
DROP TEMPORARY TABLE tmp_merge_groups_nov2026;
DROP TEMPORARY TABLE tmp_merge_members_nov2026;

DROP TEMPORARY TABLE tmp56_logs;
DROP TEMPORARY TABLE tmp56_previous;
DROP TEMPORARY TABLE tmp56_marked;
DROP TEMPORARY TABLE tmp56_anchors;
DROP TEMPORARY TABLE tmp56_grouped;
DROP TEMPORARY TABLE tmp56_targets;
DROP TEMPORARY TABLE tmp56_periods;
DROP TEMPORARY TABLE tmp56_periods_copy;
DROP TEMPORARY TABLE tmp56_ordered_periods;
DROP TEMPORARY TABLE tmp56_totals;
