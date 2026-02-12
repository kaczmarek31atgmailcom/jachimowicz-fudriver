set foreign_key_checks =0;
alter table skup_rodzaj drop foreign key skup_rodzaj_ibfk_1;
alter table skup_rodzaj drop column local_rodzaj_id;
alter table rodzaj add skup_rodzaj_id int default null;
alter table rodzaj add foreign key (skup_rodzaj_id) references skup_rodzaj(id);
set foreign_key_checks =1;
