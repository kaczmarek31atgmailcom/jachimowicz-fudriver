ALTER TABLE ludzie
  CHANGE data_waznosci_zezwolenia koniec_waznosci_zezwolenia DATETIME;
ALTER TABLE ludzie
  ADD poczatek_waznosci_zezwolenia DATETIME
  AFTER nr_wizy;
ALTER TABLE ludzie
  ADD poczatek_zameldowania DATETIME
  AFTER koniec_waznosci_zezwolenia;
ALTER TABLE ludzie
  ADD koniec_zameldowania DATETIME
  AFTER poczatek_zameldowania;
ALTER TABLE ludzie
  ADD koniec_waznosci_paszportu DATETIME
  AFTER koniec_zameldowania;
ALTER TABLE ludzie
  ADD koniec_waznosci_wizy DATETIME
  AFTER nr_wizy;
ALTER TABLE ludzie
  ADD czy_obcokrajowiec TINYINT DEFAULT 0
  AFTER active;
CREATE TABLE foreigner_alert (
  id             INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  visa_days      INT,
  statement_days INT,
  passport_days  INT,
  stay_days      INT
);

INSERT INTO foreigner_alert (visa_days, statement_days, passport_days, stay_days) VALUES (0, 0, 0, 0);