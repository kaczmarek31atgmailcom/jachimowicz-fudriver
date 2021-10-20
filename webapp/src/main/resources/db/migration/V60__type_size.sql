CREATE TABLE type_size (
  `id`     INT(11) NOT NULL      AUTO_INCREMENT,
  `uuid`   VARCHAR(60)           DEFAULT NULL,
  `name`   VARCHAR(60)
           CHARACTER SET utf8mb4 DEFAULT NULL,
  `active` TINYINT(4)            DEFAULT '1',
  PRIMARY KEY (`id`)
);

ALTER TABLE rodzaj
  ADD size_id INT;

ALTER TABLE rodzaj
  ADD FOREIGN KEY (size_id) REFERENCES type_size (id);


CREATE INDEX hala_rodzaj_time
  ON zarobki (hala_id, rodzaj_id,time);
