CREATE TABLE wage (
  id              INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  type_id         INT,
  salary_day_type INT  NOT NULL,
  start_date      DATE NOT NULL,
  end_date        DATE,
  value           INT  NOT NULL,
  FOREIGN KEY (type_id) REFERENCES rodzaj (id)
);