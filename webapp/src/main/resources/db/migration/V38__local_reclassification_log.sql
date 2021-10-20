CREATE TABLE local_reclassification_log (
  id                  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  date                TIMESTAMP,
  supplier_id         INT,
  picker_id           INT,
  uniq_id             INT,
  user_id             INT,
  login               VARCHAR(50)
                      CHARACTER SET utf8mb4,
  name                VARCHAR(50)
                      CHARACTER SET utf8mb4,
  surname             VARCHAR(50)
                      CHARACTER SET utf8mb4,
  reason_id           INT,
  reason_text         VARCHAR(300)
                      CHARACTER SET utf8mb4,
  source_type_id      INT,
  source_type_name    VARCHAR(50)
                      CHARACTER SET utf8mb4,
  source_type_weight  DOUBLE,
  target_type_id      INT,
  target_type_name    VARCHAR(50)
                      CHARACTER SET utf8mb4,
  target_type_weight  DOUBLE,
  source_cycle_id     INT,
  source_chamber_name VARCHAR(50)
                      CHARACTER SET utf8mb4,
  target_cycle_id     INT,
  target_chamber_name VARCHAR(50)
                      CHARACTER SET utf8mb4,
  FOREIGN KEY (picker_id) REFERENCES ludzie (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (reason_id) REFERENCES reclassify_reason (id),
  FOREIGN KEY (source_type_id) REFERENCES rodzaj (id),
  FOREIGN KEY (target_type_id) REFERENCES rodzaj (id),
  FOREIGN KEY (source_cycle_id) REFERENCES cykle (id),
  FOREIGN KEY (target_cycle_id) REFERENCES cykle (id)
);