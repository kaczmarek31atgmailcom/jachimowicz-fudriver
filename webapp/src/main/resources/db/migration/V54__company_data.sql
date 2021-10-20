CREATE TABLE company (
  id       INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  uuid     VARCHAR(60),
  name     VARCHAR(200)
           CHARACTER SET utf8mb4,
  street   VARCHAR(200)
           CHARACTER SET utf8mb4,
  city     VARCHAR(200)
           CHARACTER SET utf8mb4,
  nip      VARCHAR(200)
           CHARACTER SET utf8mb4,
  regon    VARCHAR(200)
           CHARACTER SET utf8mb4,
  phone_no VARCHAR(200)
           CHARACTER SET utf8mb4,
  email    VARCHAR(200)
           CHARACTER SET utf8mb4
);

INSERT INTO company (id, uuid, name, street, city, nip, regon, phone_no, email) VALUES (1, 1, '', '', '', '', '', '', '');