CREATE TABLE delivery_letter
(
  id            INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  uuid          VARCHAR(60),
  monthly_no     INT,
  truck_id      INT,
  release_date  DATE,
  creation_date DATE,
  status        INT,
  FOREIGN KEY (truck_id) REFERENCES truck (id)
);

CREATE TABLE delivery_letter_palette (
  id            INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  uuid          VARCHAR(60),
  delivery_letter_id int,
  palette_id int,
  position int,
  FOREIGN KEY (palette_id) REFERENCES palette(id),
  UNIQUE delivery_letter_position (delivery_letter_id,position)
);


DELIMITER $$
CREATE TRIGGER DELIVERY_LETTER_MONTHLY_NUMBER_TRIGGER
BEFORE INSERT ON delivery_letter
FOR EACH ROW
  BEGIN
    SET NEW.monthly_no = (
      SELECT IFNULL(MAX(monthly_no), 0) + 1
      FROM delivery_letter
      WHERE DATE_FORMAT(creation_date, '%Y%m') = DATE_FORMAT(curdate(), '%Y%m')
    );
  END $$
DELIMITER ;