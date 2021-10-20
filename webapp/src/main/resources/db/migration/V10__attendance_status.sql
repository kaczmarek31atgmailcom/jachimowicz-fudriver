create table attendance_status (id int not null AUTO_INCREMENT PRIMARY KEY,
ludzie_id int not null,
is_present tinyint default 0,
FOREIGN KEY ludzie_id_fk (ludzie_id) REFERENCES ludzie(id));
