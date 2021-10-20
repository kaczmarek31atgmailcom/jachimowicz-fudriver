CREATE TABLE active_currency (
  id          INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  uuid        VARCHAR(60),
  code        VARCHAR(5)
              CHARACTER SET utf8mb4,
  order_index INT,
  UNIQUE (code),
  UNIQUE (order_index)
);