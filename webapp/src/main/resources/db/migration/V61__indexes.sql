alter table wozek add unique (nr_rwaczki,uniq_id);
create index ludzie_timeshort on zarobki (ludzie_id, timeshort);
alter table chlodnia CHANGE time date date default '0000-00-00';
ALTER TABLE chlodnia CHANGE ilosc ilosc double (30,3);