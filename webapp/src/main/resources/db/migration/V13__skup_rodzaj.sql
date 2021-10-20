create table skup_rodzaj (id int not null primary key auto_increment, remote_id int not null, name varchar(50), weight double not null, local_rodzaj_id int, description varchar(500),
foreign key rodzaj_fk(local_rodzaj_id) references rodzaj(id));
alter table skup_rodzaj add index remote_id_idx (remote_id);