set foreign_key_checks=0;
drop table if exists time_work_log;
drop table if exists attendance_status;
set foreign_key_checks=1;
create table time_work_log (id int not null primary key auto_increment, person_id int not null, start_time datetime not null, end_time datetime, isOpened tinyint default 1);
alter table time_work_log add constraint foreign key person_fk  (person_id) references ludzie (id);
create index time_work_log_opened on time_work_log(isOpened);