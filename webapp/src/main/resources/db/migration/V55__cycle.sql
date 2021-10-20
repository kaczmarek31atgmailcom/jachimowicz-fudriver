alter table hala add uuid VARCHAR(60) after id;
update hala set uuid = id;
alter table chlodnie add uuid VARCHAR(60) after id;
alter table hala change name name varchar(40) CHARACTER SET utf8mb4;
UPDATE chlodnie set uuid = id;
alter table chlodnie change name name varchar(40) CHARACTER SET utf8mb4;
alter table kompostownia add uuid VARCHAR(60) after id;
alter table kompostownia change nazwa nazwa varchar(40) CHARACTER SET utf8mb4;
UPDATE kompostownia set uuid = id;
alter table grzybnia add uuid VARCHAR(60) after id;
UPDATE grzybnia set uuid = id;
alter table grzybnia change nazwa nazwa varchar(40) CHARACTER SET utf8mb4;
alter table cykle add uuid VARCHAR(60) after id;
update cykle set uuid = id;
alter table cykle add column technolog_id int default 1 after wilgotnosc;
alter table cykle add column version int default 0 after technolog_id;
UPDATE cykle set version = 0;

update cykle set technolog_id = 1 where technolog_id is null;
update cykle set hala_id =1 where hala_id is null;
update cykle set kompostownia_id = 1 where kompostownia_id is null;
UPDATE cykle set grzybnia_id = 1 where grzybnia_id is null;

alter table cykle add constraint technolog_fk FOREIGN KEY (technolog_id) references users(id);
alter table cykle add constraint hala_fk FOREIGN KEY (hala_id) references hala(id);
alter table cykle add constraint grzybnia_fk FOREIGN KEY (grzybnia_id) references grzybnia(id);
alter table cykle add constraint kompostownia_fk FOREIGN KEY (kompostownia_id) references kompostownia(id)

