-- MySQL 8.0. Uruchom caly plik w jednej sesji, w wybranej bazie aplikacji.
-- Przelicza tylko ISTNIEJACE wpisy zarobki z listopada 2026 (bez INSERT/DELETE).
-- godziny_pracy zawiera MINUTY. czy_akord: 0 = godzinowy, 1 = akordowy.
-- Akord: od pierwszego startu do ostatniego konca, minus 30 min gdy > 210 min.
-- Godzinowy: suma zamknietych okresow, +15 min raz przy >= 3 okresach.
-- Okresy przez polnoc dzielone na dni; sekundy sumowane przed obcieciem minut.
-- Pomija caly dzien, jesli jakikolwiek jego wpis ma zaplacono=1.
-- Pomija dni z nakladajacymi sie okresami lub niepoprawnym typem pracownika.
-- Nakladajace sie dane nalezy najpierw poprawic w time_work_log.
-- Dzien bez zamknietych okresow otrzymuje 0 minut.
-- Suma trafia do jednego istniejacego wpisu; pozostale minuty sa zerowane.
-- timeshort dla przeliczanych wpisow jest ustawiany na 202611.
-- Domyslnie ROLLBACK: podglad i proba bez trwalej zmiany danych.
-- Aby zapisac wynik, zamien koncowy ROLLBACK na COMMIT i uruchom ponownie.
-- Uruchamiaj bez rownoleglych rozliczen placowych. Przy bledzie przerwij i ROLLBACK.

DROP TEMPORARY TABLE IF EXISTS tmp_payroll_nov2026;
START TRANSACTION;

-- Ta sama kolejnosc blokad co w aplikacji: najpierw pracownik.
SELECT p.id AS locked_person_id
FROM ludzie p
WHERE EXISTS (SELECT 1 FROM zarobki z WHERE z.ludzie_id=p.id
              AND z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01')
ORDER BY p.id FOR UPDATE;

SELECT z.id AS locked_earnings_id
FROM zarobki z
WHERE z.`time` >= '2026-11-01' AND z.`time` < '2026-12-01'
ORDER BY z.ludzie_id, z.id FOR UPDATE;

CREATE TEMPORARY TABLE tmp_payroll_nov2026 AS
WITH targets AS (
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
), periods AS (
    SELECT k.person_id, k.work_day, t.id,
           GREATEST(t.start_time, CAST(k.work_day AS DATETIME)) AS period_start,
           LEAST(t.end_time, CAST(k.work_day AS DATETIME)+INTERVAL 1 DAY) AS period_end
    FROM targets k JOIN time_work_log t ON t.person_id=k.person_id
      AND t.isOpened=0 AND t.end_time > t.start_time
      AND t.start_time < CAST(k.work_day AS DATETIME)+INTERVAL 1 DAY
      AND t.end_time > CAST(k.work_day AS DATETIME)
), ordered_periods AS (
    SELECT periods.*,
           MAX(period_end) OVER (PARTITION BY person_id, work_day
               ORDER BY period_start, id ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING) AS previous_end
    FROM periods
), totals AS (
    SELECT person_id, work_day, COUNT(*) AS period_count,
           TIMESTAMPDIFF(MINUTE, MIN(period_start), MAX(period_end)) AS span_minutes,
           FLOOR(SUM(TIMESTAMPDIFF(MICROSECOND, period_start, period_end))/60000000) AS worked_minutes,
           MAX(CASE WHEN period_start < previous_end THEN 1 ELSE 0 END) AS overlaps
    FROM ordered_periods GROUP BY person_id, work_day
)
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
FROM targets k LEFT JOIN ludzie p ON p.id=k.person_id
LEFT JOIN totals t ON t.person_id=k.person_id AND t.work_day=k.work_day;

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

ROLLBACK;
DROP TEMPORARY TABLE tmp_payroll_nov2026;
