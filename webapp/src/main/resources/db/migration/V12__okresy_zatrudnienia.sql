CREATE TABLE okresy_zatrudnienia (
id int not null primary key auto_increment,
ludzie_id int NOT NULL,
startdate datetime,
enddate datetime,
FOREIGN KEY ludzie_id_fk1(ludzie_id) REFERENCES ludzie(id) );