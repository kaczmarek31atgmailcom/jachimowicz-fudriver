create table uniq (id int not null primary key auto_increment, ludzie_id int not null, uniq_id int not null, used tinyint default 0);
alter table uniq add UNIQUE ludzie_uniq (ludzie_id,uniq_id);