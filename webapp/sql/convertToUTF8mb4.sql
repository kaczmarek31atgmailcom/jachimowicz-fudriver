alter table ludzie modify column imie varchar(30) character set utf8mb4;
alter table ludzie modify column nazwisko varchar(30) character set utf8mb4;
alter table ludzie modify column adres varchar(40) character set utf8mb4;
alter table ludzie modify column nr_telefonu varchar(30) character set utf8mb4;
alter table ludzie modify column imiona_rodzicow varchar(50) character set utf8mb4;
alter table ludzie modify column indeks varchar(40) character set utf8mb4;
alter table ludzie modify column miasto varchar(40) character set utf8mb4;
alter table ludzie modify column nr_wizy varchar(40) character set utf8mb4;
alter table ludzie modify column obwod varchar(40) character set utf8mb4;
alter table ludzie modify column pesel varchar(30) character set utf8mb4;
alter table ludzie modify column rejon varchar(40) character set utf8mb4;

alter table users modify column user_name varchar(30) character set utf8mb4;
alter table users modify column passwd varchar(50) character set utf8mb4;
alter table users modify column imie varchar(30) character set utf8mb4;
alter table users modify column nazwisko varchar(30) character set utf8mb4;

alter table rodzaj modify column name varchar(30) character set utf8mb4;

alter table chlodnie modify column name varchar(30) character set utf8mb4;

alter table config modify column name varchar(60) character set utf8mb4;
alter table config modify column value varchar(40) character set utf8mb4;

alter table dokarmiacz_okrywy modify column nazwa varchar(30) character set utf8mb4;
alter table dokarmiacz_podloza modify column nazwa varchar(30) character set utf8mb4;

alter table grupy modify column name varchar(30) character set utf8mb4;

alter table grzybnia modify column nazwa varchar(30) character set utf8mb4;

alter table hala modify column name varchar(30) character set utf8mb4;

alter table kaking modify column nazwa varchar(50) character set utf8mb4;
alter table kompostownia modify column nazwa varchar(30) character set utf8mb4;

alter table nagrody_dict modify column name varchar(40) character set utf8mb4;

alter table palety modify column name varchar(30) character set utf8mb4;

alter table rodzaj modify column name varchar(20) character set utf8mb4;

alter table skrz_cust modify column name varchar(100) character set utf8mb4;
alter table skrz_rodzaj modify column name varchar(100) character set utf8mb4;

alter table skup_rodzaj modify column name varchar(50) character set utf8mb4;
alter table skup_rodzaj modify column description varchar(50) character set utf8mb4;

alter table stawki modify column name varchar(40) character set utf8mb4;
alter table stopien_ubicia modify column nazwa varchar(50) character set utf8mb4;

alter table torf modify column nazwa varchar(50) character set utf8mb4;

alter table user_roles modify column username varchar(45) character set utf8mb4;
alter table user_roles modify column ROLE varchar(45) character set utf8mb4;
















