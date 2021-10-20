
CREATE TABLE wz_brc_header (
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  month_number INT NOT NULL,
  time DATE NOT NULL ,
  creator_id INT NOT NULL,
  customer_id INT NOT NULL,
  FOREIGN KEY (creator_id) REFERENCES users(id),
  FOREIGN KEY (customer_id) REFERENCES skrz_cust(id)
);

create index uniq_id_idx on zarobki(uniq_id);
create table wz_brc_detail (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  header_id INT NOT NULL,
  picker_id INT NOT NULL,
  uniq_id INT NOT NULL,
  rodzaj_id INT NOT NULL,
  rodzaj_name VARCHAR(30),
  weight DOUBLE(8,3) NOT NULL ,
  cycle_id INT NOT NULL,
  hala_name VARCHAR(30),
  harvest_date DATE NOT NULL,
  FOREIGN KEY (header_id) REFERENCES wz_brc_header(id),
  FOREIGN KEY (picker_id) REFERENCES ludzie(id),
  FOREIGN KEY (uniq_id) REFERENCES zarobki(uniq_id),
  FOREIGN KEY (rodzaj_id) REFERENCES rodzaj(id),
  FOREIGN KEY (cycle_id) REFERENCES cykle(id),
  UNIQUE KEY (picker_id, uniq_id)
);

CREATE TABLE wz_brc_palette (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  header_id INT NOT NULL ,
  palette_id INT NOT NULL ,
  palette_name VARCHAR(30),
  amount INT NOT NULL,
  FOREIGN KEY (header_id) REFERENCES wz_brc_header(id),
  FOREIGN KEY (palette_id) REFERENCES palety(id)
);