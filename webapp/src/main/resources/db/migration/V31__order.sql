CREATE TABLE orders (
  id            INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  customer_id   INT      NOT NULL,
  created_date  DATETIME NOT NULL,
  created_by    INT      NOT NULL,
  required_date DATETIME,
  released_date DATETIME,
  status        INT,
  FOREIGN KEY (customer_id) REFERENCES skrz_cust (id),
  FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE palette (
  id           INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  order_id     INT      NOT NULL,
  status       INT      NOT NULL,
  created_date DATETIME NOT NULL,
  created_by   INT      NOT NULL,
  FOREIGN KEY (created_by) REFERENCES users (id),
  FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE batch (
  id              INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  type_id         INT      NOT NULL,
  amount          INT      NOT NULL,
  reserved_amount INT                           DEFAULT 0,
  palette_id      INT      NOT NULL,
  status          INT      NOT NULL,
  created_date    DATETIME NOT NULL,
  created_by      INT      NOT NULL,
  FOREIGN KEY (created_by) REFERENCES users (id),
  FOREIGN KEY (palette_id) REFERENCES palette (id),
  FOREIGN KEY (type_id) REFERENCES rodzaj (id)
);

ALTER TABLE warehouse_brc ADD batch_id INT;
ALTER TABLE warehouse_brc ADD FOREIGN KEY (batch_id) REFERENCES batch (id);