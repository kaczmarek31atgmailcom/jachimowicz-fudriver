ALTER TABLE ludzie ADD regular_wage INT AFTER rfid;
ALTER TABLE ludzie ADD sunday_wage INT AFTER regular_wage;
ALTER TABLE ludzie ADD bonus_wage INT after sunday_wage;
