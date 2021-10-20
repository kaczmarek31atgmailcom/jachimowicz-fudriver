CREATE TABLE wage_header (
  id     INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  uuid   VARCHAR(60),
  name   VARCHAR(200)
         CHARACTER SET utf8mb4,
  active INT
);

ALTER TABLE wage
  DROP COLUMN start_date;
ALTER TABLE wage
  DROP COLUMN end_date;
ALTER TABLE wage
  ADD header_id INT NOT NULL;
ALTER TABLE wage
  ADD CONSTRAINT wage_header_fk FOREIGN KEY (header_id) REFERENCES wage_header (id);

ALTER TABLE ludzie
  ADD COLUMN wage_header_id INT
  AFTER bonus_wage;
ALTER TABLE ludzie
  ADD FOREIGN KEY (wage_header_id) REFERENCES wage_header (id);


SELECT
  z.ludzie_id            AS personId,
  w.value                AS wage,
  w.salary_day_type      AS dayType,
  sum(z.ilosc) * 1000    AS totalKg,
  sum(z.ilosc) * w.value AS salary
FROM zarobki z, wage w, ludzie l, calendar c
WHERE l.wage_header_id = w.header_id AND z.rodzaj_id = w.type_id AND z.ludzie_id = l.id AND c.date = z.time AND c.editable = w.salary_day_type AND
      z.timeshort = '201707' AND l.czy_akord = 1 AND z.time = c.date
GROUP BY z.ludzie_id, w.value, w.salary_day_type;