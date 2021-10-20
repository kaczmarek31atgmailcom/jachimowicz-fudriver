create table east_mushrooms_warehouse_palette (
  id            int not null primary key auto_increment,
  uuid          VARCHAR(60)              DEFAULT NULL,
  creator_id    int not null,
  creation_time timestamp                default current_timestamp,
  depot_id      int not null,
  status        int,
  version       int default 0,
  foreign key (creator_id) references users (id),
  foreign key (depot_id) references chlodnie (id)
);

create table east_mushrooms_warehouse_unit (
  id             int  not null primary key auto_increment,
  uuid           VARCHAR(60)               DEFAULT NULL,
  palette_id     int,
  picker_id      int  not null,
  local_type_id  int  not null,
  remote_type_id int,
  uniq_id        int  not null,
  harvest_time   date not null,
  scanner_man_id int  not null,
  cycle_id       int  not null,
  harvest_palette_id int not null,
  status         int  not null,
  version        int default 0,
  foreign key (palette_id) references east_mushrooms_warehouse_palette (id),
  foreign key (picker_id) references ludzie (id),
  foreign key (local_type_id) references rodzaj (id),
  foreign key (remote_type_id) references skup_rodzaj(remote_id),
  foreign key (scanner_man_id) references users (id),
  foreign key (cycle_id) references cykle (id)
);

insert into config values (27, 'Chlodnia East Mushrooms', 0, 0);

create table east_mushrooms_wz_document(
  id              int not null primary key auto_increment,
  uuid            VARCHAR(60) DEFAULT NULL,
  monthly_no      int,
  company_name    varchar(60) CHARACTER SET utf8mb4,
  company_street  varchar(60) CHARACTER SET utf8mb4,
  company_city    varchar(60) CHARACTER SET utf8mb4,
  company_nip     varchar(60) CHARACTER SET utf8mb4,
  company_ggn     varchar(60) CHARACTER SET utf8mb4,
  customer_id     int,
  customer_name   varchar(60) CHARACTER SET utf8mb4,
  date            date,
  creator_id      int,
  creator_login   varchar(60) CHARACTER SET utf8mb4,
  creator_name    varchar(60) CHARACTER SET utf8mb4,
  creator_surname varchar(60) CHARACTER SET utf8mb4
);

DELIMITER $$
CREATE TRIGGER EAST_MUSHROOMS_WZ_MONTHLY_NUMBER_TRIGGER
  BEFORE INSERT ON east_mushrooms_wz_document
  FOR EACH ROW
  BEGIN
    SET NEW.monthly_no = (
                         SELECT IFNULL(MAX(monthly_no), 0) + 1
                         FROM east_mushrooms_wz_document
                         WHERE DATE_FORMAT(date, '%Y%m') = DATE_FORMAT(curdate(), '%Y%m')
                         );
  END $$
DELIMITER ;

create table east_mushrooms_wz_palette(
  id                                int not null primary key auto_increment,
  uuid                              VARCHAR(60) DEFAULT NULL,
  wz_document_id                    int not null,
  warehouse_palette_id              int not null,
  warehouse_palette_creation_date   date not null,
  warehouse_palette_creator_id      int not null,
  warehouse_palette_creator_login   VARCHAR(60) CHARACTER SET utf8mb4 not null,
  warehouse_palette_creator_name    VARCHAR(60) CHARACTER SET utf8mb4 DEFAULT NULL,
  warehouse_palette_creator_surname VARCHAR(60) CHARACTER SET utf8mb4 DEFAULT NULL,
  foreign key (wz_document_id) references east_mushrooms_wz_document(id),
  foreign key (warehouse_palette_id) references east_mushrooms_warehouse_palette(id)
);

create table east_mushrooms_wz_unit(
  id                     int  not null primary key auto_increment,
  uuid                   VARCHAR(60) DEFAULT NULL,
  wz_palette_id          int not null,
  uniq_id                int not null,
  local_type_id          int not null,
  local_type_name        VARCHAR(60) CHARACTER SET utf8mb4 default null,
  local_type_weight      double not null,
  remote_type_id         int,
  remote_type_name       VARCHAR(60) CHARACTER SET utf8mb4 default null,
  remote_type_weight     double,
  harvest_date           date not null,
  cycle_id               int not null,
  chamber_name           VARCHAR(60) CHARACTER SET utf8mb4 default null,
  picker_id              int,
  picker_name            VARCHAR(60) CHARACTER SET utf8mb4 default null,
  picker_surname         VARCHAR(60) CHARACTER SET utf8mb4 default null,
  foreign key (wz_palette_id) references east_mushrooms_wz_palette(id)
);

CREATE TABLE producer_group (
  id int(11) NOT NULL AUTO_INCREMENT,
  uuid varchar(60) DEFAULT NULL,
  name varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  address varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  active int(11) DEFAULT 1,
  PRIMARY KEY (id)
);
alter table skrz_cust add producer_group_id int default 0;
alter table company add ggn varchar(60) CHARACTER SET utf8mb4 default null after email;
