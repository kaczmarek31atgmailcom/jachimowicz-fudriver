CREATE TABLE rozliczenia (
  id int(11) NOT NULL AUTO_INCREMENT,
  nr_dok int(11) DEFAULT NULL,
  ludzie_id int(11) NOT NULL DEFAULT '0',
  data_od date NOT NULL,
  data_do date NOT NULL,
  data date NOT NULL,
  kwota float(8,2) DEFAULT NULL,
  stawka_id int(11) DEFAULT NULL,
  export float(8,2) DEFAULT NULL,
  kraj float(8,2) DEFAULT NULL,
  stala_placowa_id tinyint(4) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY nr_dok (nr_dok),
  KEY ludzie_id (ludzie_id),
  KEY stawka (stawka_id)
);

CREATE TABLE rozliczenia_kg (
  id int(11) NOT NULL AUTO_INCREMENT,
  nr_dok int(11) DEFAULT NULL,
  ludzie_id int(11) NOT NULL DEFAULT '0',
  export double(8,2) DEFAULT NULL,
  kraj double(8,2) DEFAULT NULL,
  data_od date NOT NULL,
  data_do date NOT NULL,
  data date NOT NULL,
  PRIMARY KEY (id),
  KEY nr_dok (nr_dok),
  KEY ludzie_id (ludzie_id)
);

CREATE TABLE rozliczenia_month (
  id int(11) NOT NULL AUTO_INCREMENT,
  nr_dok int(11) DEFAULT NULL,
  ludzie_id int(11) DEFAULT NULL,
  timeshort int(11) DEFAULT NULL,
  data timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  rodzaj_id int(11) DEFAULT NULL,
  stawka_id int(11) DEFAULT NULL,
  suma_kg float(8,3) DEFAULT NULL,
  stala_placowa_id int(11) DEFAULT NULL,
  bonus_id int(11) DEFAULT NULL,
  kwota float(8,2) DEFAULT NULL,
  user_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY timeshort (timeshort)
);


CREATE TABLE skrz_cust (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) DEFAULT NULL,
  active int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE skrz_mag (
  id int(11) NOT NULL AUTO_INCREMENT,
  rodzaj_id int(11) DEFAULT NULL,
  ilosc int(11) DEFAULT NULL,
  customer_id int(11) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE skrz_rodzaj (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) DEFAULT NULL,
  active int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE skrz_trs_action (
  id int(11) NOT NULL AUTO_INCREMENT,
  rodzaj_id int(11) DEFAULT NULL,
  customer_id int(11) DEFAULT NULL,
  trans_id int(11) DEFAULT NULL,
  amount int(11) DEFAULT NULL,
  time date NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE skrz_trs_type (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) DEFAULT NULL,
  sign int(11) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE stale_placowe (
  id int(11) NOT NULL AUTO_INCREMENT,
  ludzie_id int(11) DEFAULT NULL,
  stala_placowa_id int(11) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE stale_placowe_dict (
  id int(11) NOT NULL AUTO_INCREMENT,
  value float(8,2) DEFAULT NULL,
  name varchar(30) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE stawki (
  id int(11) NOT NULL AUTO_INCREMENT,
  value float(8,2) DEFAULT '0.00',
  name varchar(40) DEFAULT NULL,
  active int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE stopien_ubicia (
  id int(11) NOT NULL AUTO_INCREMENT,
  nazwa varchar(50) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE torf (
  id int(11) NOT NULL AUTO_INCREMENT,
  nazwa varchar(50) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE trans_type (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(40) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE trs (
  id int(11) NOT NULL AUTO_INCREMENT,
  customer_id int(11) DEFAULT NULL,
  trans_type_id int(11) DEFAULT NULL,
  rodzaj_id int(11) DEFAULT NULL,
  time date,
  ilosc float(8,3) DEFAULT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE users (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_name varchar(30) DEFAULT NULL,
  passwd varchar(50) DEFAULT NULL,
  imie varchar(30) DEFAULT NULL,
  nazwisko varchar(30) DEFAULT NULL,
  active int(11) DEFAULT NULL,
  removed int(11) DEFAULT NULL,
  waga int(11) DEFAULT '0',
  palety int(11) DEFAULT '0',
  panel int(11) DEFAULT '0',
  rozliczenia int(11) DEFAULT '0',
  dostepy int(11) DEFAULT '0',
  brygadzista int(11) DEFAULT '0',
  PRIMARY KEY (id),
  KEY user_name (user_name)
);


CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  user_id int(11) NOT NULL,
  ROLE varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (ROLE,user_id),
  KEY fk_user_id_idx (user_id),
  CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);


insert into users(id,user_name,passwd,imie,nazwisko,active,removed,waga,palety,panel,rozliczenia,dostepy,brygadzista) values(1,'admin','520abe2acc8f13cafaa04b932c7c51ab','konto administratora','',1,0,1,1,1,1,1,1);
insert into user_roles (user_role_id,username,user_id,role) values (1,'admin',1,'ROLE_WAGA');
insert into user_roles (user_role_id,username,user_id,role) values (2,'admin',1,'ROLE_PALETA');
insert into user_roles (user_role_id,username,user_id,role) values (3,'admin',1,'ROLE_PANEL');
insert into user_roles (user_role_id,username,user_id,role) values (4,'admin',1,'ROLE_ROZLICZENIA');
insert into user_roles (user_role_id,username,user_id,role) values (5,'admin',1,'ROLE_DOSTEPY');
insert into user_roles (user_role_id,username,user_id,role) values (6,'admin',1,'ROLE_LEADER');


CREATE TABLE work_status (
  id int(11) NOT NULL AUTO_INCREMENT,
  zbieraczka_id int(11) DEFAULT NULL,
  time datetime DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE wozek (
  id int(11) NOT NULL AUTO_INCREMENT,
  nr_rwaczki int(11) DEFAULT NULL,
  rodzaj_id int(11) DEFAULT NULL,
  ilosc double(8,3) DEFAULT NULL,
  time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  hala int(11) DEFAULT NULL,
  wozek_nr int(11) DEFAULT '0',
  przestrzen_id tinyint(4) DEFAULT '0',
  user_id int(11) DEFAULT NULL,
  uniq_id int(5) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE wozek_hist (
  id int(11) NOT NULL AUTO_INCREMENT,
  wozek_id int(11) DEFAULT NULL,
  rodzaj int(11) DEFAULT NULL,
  ilosc double(8,2) DEFAULT NULL,
  time date NOT NULL,
  zbior int(11) NOT NULL DEFAULT '0',
  hala int(11) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE wozek_seq (
  id int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

CREATE TABLE wz_detail (
  id int(11) NOT NULL AUTO_INCREMENT,
  header_id int(11) DEFAULT NULL,
  rodzaj_id int(11) DEFAULT NULL,
  paleta_id int(11) DEFAULT NULL,
  ilosc float(8,3) DEFAULT NULL,
  cena float(8,2) DEFAULT '0.00',
  PRIMARY KEY (id)
);

CREATE TABLE wz_header (
  id int(11) NOT NULL AUTO_INCREMENT,
  nr_short int(11) DEFAULT NULL,
  nr_full varchar(30) DEFAULT NULL,
  time date DEFAULT NULL,
  timeshort int(6) DEFAULT NULL,
  user_id int(11) DEFAULT NULL,
  customer_id int(11) DEFAULT NULL,
  chlodnia_id int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE zamowienia (
  id int(11) NOT NULL AUTO_INCREMENT,
  rodzaj_id int(11) DEFAULT NULL,
  ilosc double(9,3) DEFAULT '0.000',
  PRIMARY KEY (id)
);

CREATE TABLE zarobki (
  id int(11) NOT NULL AUTO_INCREMENT,
  ludzie_id int(11) DEFAULT NULL,
  export double(8,3) DEFAULT '0.000',
  kraj double(8,3) DEFAULT '0.000',
  godziny_pracy int(11) DEFAULT NULL,
  godziny_dodatkowe int(11) DEFAULT NULL,
  hala_id int(11) DEFAULT NULL,
  time date NOT NULL,
  rodzaj_id int(11) DEFAULT NULL,
  zaplacono tinyint(4) DEFAULT '0',
  wozek_nr int(11) DEFAULT NULL,
  timeshort int(6) DEFAULT NULL,
  user_id int(11) DEFAULT NULL,
  uniq_id int(5) DEFAULT NULL,
  ilosc double(8,3) DEFAULT '0.000',
  PRIMARY KEY (id),
  UNIQUE KEY ludzie_uniq (ludzie_id,uniq_id),
  KEY in_ludzie (ludzie_id) USING BTREE,
  KEY rodzaj (rodzaj_id),
  KEY time (time),
  KEY hala_id (hala_id),
  KEY time_short (timeshort),
  KEY wozek_nr (wozek_nr),
  KEY hala_id_time_short (hala_id,timeshort),
  KEY rodzaj_id_ludzie_id_time_short (rodzaj_id,ludzie_id,timeshort),
  KEY time_ludzie_id (time,ludzie_id),
  KEY ludzie_rodzaj (ludzie_id,rodzaj_id),
  KEY ludzie_time (ludzie_id,time),
  KEY zaplacono (zaplacono),
  KEY zaplacono_time (zaplacono,time)
);


create  VIEW sumy_zarobki_v AS select zarobki.hala_id AS cykle_id,sum(zarobki.export) AS export,sum(zarobki.kraj) AS kraj from zarobki group by zarobki.hala_id;
create VIEW zarobki_grzybnia_v AS select zarobki.time AS time,cykle.kompostownia_id AS kompostownia_id,cykle.grzybnia_id AS grzybnia_id,sum(zarobki.export) AS export,sum(zarobki.kraj) AS kraj from (zarobki join cykle) where (cykle.id = zarobki.hala_id) group by zarobki.time,cykle.kompostownia_id,cykle.grzybnia_id;
