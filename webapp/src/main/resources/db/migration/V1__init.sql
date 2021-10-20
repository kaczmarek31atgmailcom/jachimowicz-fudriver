CREATE TABLE calendar (
  id int(11) NOT NULL AUTO_INCREMENT,
  date date,
  editable int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE chlodnia (
  id int(11) NOT NULL AUTO_INCREMENT,
  rodzaj_id int(11) DEFAULT NULL,
  ilosc double(8,3) DEFAULT NULL,
  time date NOT NULL,
  chlodnia_id int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE chlodnia_hist (
  id int(11) NOT NULL AUTO_INCREMENT,
  rodzaj_id int(11) DEFAULT NULL,
  ilosc double(10,3) DEFAULT NULL,
  time date,
  chlodnia_id int(11) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE chlodnie (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(30) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE config (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(60) DEFAULT NULL,
  value varchar(40) DEFAULT NULL,
  enabled int(11) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE cykle (
  id int(11) NOT NULL AUTO_INCREMENT,
  hala_id int(11) DEFAULT NULL,
  poczatek int(11) DEFAULT NULL,
  koniec_1 int(11) DEFAULT NULL,
  koniec_2 int(11) DEFAULT NULL,
  koniec int(11) DEFAULT NULL,
  aktywna int(11) DEFAULT NULL,
  ile_ton int(11) DEFAULT NULL,
  kompostownia_id int(11) DEFAULT NULL,
  grzybnia_id int(11) DEFAULT NULL,
  ile_metrow int(11) DEFAULT NULL,
  description text,
  dok_okrywy_id int(11) DEFAULT '1',
  dok_podloza_id int(11) DEFAULT '1',
  kaking_id int(11) DEFAULT '1',
  stopien_ubicia_id int(11) DEFAULT '1',
  torf_id int(11) DEFAULT '1',
  wilgotnosc int(11) DEFAULT '20',
  PRIMARY KEY (id),
  KEY hala_id (hala_id),
  KEY aktywna (aktywna)
);

CREATE TABLE dokarmiacz_okrywy (
  id int(11) NOT NULL AUTO_INCREMENT,
  nazwa varchar(50) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE dokarmiacz_podloza (
  id int(11) NOT NULL AUTO_INCREMENT,
  nazwa varchar(50) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE grupy (
  id int(11) NOT NULL AUTO_INCREMENT,
  nr int(11) DEFAULT NULL,
  name varchar(30) DEFAULT NULL,
  active int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE grupy_rodzaje (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(30) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE grzybnia (
  id int(11) NOT NULL AUTO_INCREMENT,
  nazwa varchar(30) DEFAULT NULL,
  active int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE hala (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(20) DEFAULT NULL,
  aktywna int(11) DEFAULT NULL,
  powierzchnia int(11) DEFAULT NULL,
  chlodnia_id int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE kaking (
  id int(11) NOT NULL AUTO_INCREMENT,
  nazwa varchar(50) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE kompostownia (
  id int(11) NOT NULL AUTO_INCREMENT,
  nazwa varchar(30) DEFAULT NULL,
  active int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE ludzie (
  id int(11) NOT NULL AUTO_INCREMENT,
  imie varchar(30) DEFAULT NULL,
  nazwisko varchar(30) DEFAULT NULL,
  adres varchar(40) DEFAULT NULL,
  nr_telefonu varchar(30) DEFAULT NULL,
  nr int(11) DEFAULT NULL,
  grupa int(11) DEFAULT NULL,
  password text,
  PRIMARY KEY (id),
  KEY nr (nr),
  KEY grupa (grupa)
);

CREATE TABLE nagrody_dict (
  id int(11) NOT NULL AUTO_INCREMENT,
  value float(8,2) DEFAULT NULL,
  name varchar(40) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE palety (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(30) DEFAULT NULL,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE przeklasyfikowania (
  id int(11) NOT NULL AUTO_INCREMENT,
  rodzaj_id_przed int(11) DEFAULT NULL,
  rodzaj_id_po int(11) DEFAULT NULL,
  ilosc float(8,3) DEFAULT NULL,
  user_id int(11) DEFAULT NULL,
  time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  chlodnia_id int(11) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE przesuniecia_miedzy_chlodniami (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) DEFAULT NULL,
  chlodnia_id_from int(11) DEFAULT NULL,
  chlodnia_id_to int(11) DEFAULT NULL,
  rodzaj_id int(11) DEFAULT NULL,
  ilosc float(8,3) DEFAULT NULL,
  time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE przesuniecia_miedzy_halami (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) DEFAULT NULL,
  hala_id_from int(11) DEFAULT NULL,
  hala_id_to int(11) DEFAULT NULL,
  wozek_nr int(11) DEFAULT NULL,
  time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE pz_detail (
  id int(11) NOT NULL AUTO_INCREMENT,
  header_id int(11) DEFAULT NULL,
  rodzaj_id int(11) DEFAULT NULL,
  paleta_id int(11) DEFAULT NULL,
  ilosc float(8,3) DEFAULT NULL,
  cena float(8,2) DEFAULT '0.00',
  PRIMARY KEY (id)
);

CREATE TABLE pz_header (
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

CREATE TABLE rodzaj (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(20) DEFAULT NULL,
  waga float DEFAULT NULL,
  wyswietl int(11) DEFAULT NULL,
  export int(11) DEFAULT NULL,
  archiwalne int(11) DEFAULT '0',
  skrzynka_id int(11) DEFAULT '1',
  stawka_id int(11) DEFAULT '1',
  grupa_id int(11) DEFAULT '1',
  PRIMARY KEY (id)
);
