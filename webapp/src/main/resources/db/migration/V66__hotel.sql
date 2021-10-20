CREATE TABLE hotel (
  id     INT(11) NOT NULL      AUTO_INCREMENT,
  uuid   VARCHAR(60)           DEFAULT NULL,
  name   VARCHAR(60)
         CHARACTER SET utf8mb4 DEFAULT NULL,
  active TINYINT(4)            DEFAULT 1,
  PRIMARY KEY (id)
);

CREATE TABLE hotel_room (
  id       INT(11) NOT NULL      AUTO_INCREMENT,
  uuid     VARCHAR(60)           DEFAULT NULL,
  name     VARCHAR(60)
           CHARACTER SET utf8mb4 DEFAULT NULL,
  hotel_id INT     NOT NULL,
  active   TINYINT(4)            DEFAULT 1,
  PRIMARY KEY (id),
  FOREIGN KEY (hotel_id) references hotel (id)
);

CREATE TABLE hotel_bed (
  id      INT(11) NOT NULL      AUTO_INCREMENT,
  uuid    VARCHAR(60)           DEFAULT NULL,
  name    VARCHAR(60)
          CHARACTER SET utf8mb4 DEFAULT NULL,
  room_id INT     NOT NULL,
  active  TINYINT(4)            DEFAULT 1,
  PRIMARY KEY (id),
  FOREIGN KEY (room_id) references hotel_room (id)
);

CREATE TABLE hotel_reservation (
  id           INT(11) NOT NULL      AUTO_INCREMENT,
  uuid         VARCHAR(60)           DEFAULT NULL,
  start_date   DATE NOT NULL,
  end_date     DATE NOT NULL,
  bed_id       INT(11) NOT NULL,
  hotel_id     INT(11) NOT NULL,
  type         INT(11) NOT NULL,
  person_id    INT(11)               DEFAULT NULL,
  description  TEXT
               CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (person_id) references ludzie (id),
  foreign key (bed_id) references hotel_bed(id),
  foreign key (hotel_id) references hotel(id)
);


CREATE TABLE hotel_bed_occupancy (
  id             INT(11) NOT NULL  AUTO_INCREMENT,
  uuid           VARCHAR(60)       DEFAULT NULL,
  reservation_id INT(11),
  date           DATE NOT NULL,
  bed_id         INT(11) NOT NULL,
  status         INT(11) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (bed_id) references hotel_bed (id),
  FOREIGN KEY (reservation_id) references hotel_reservation (id),
  UNIQUE KEY (bed_id, date)
);
