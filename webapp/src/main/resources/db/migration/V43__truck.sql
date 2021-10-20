CREATE TABLE truck (
  id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  uuid        VARCHAR(60),
  capacity    INT,
  plate       VARCHAR(15),
  description VARCHAR(500),
  active      TINYINT,
  CONSTRAINT plate UNIQUE (plate)
);
