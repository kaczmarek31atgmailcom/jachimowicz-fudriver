create table skup_reclassification(id int not null primary key auto_increment,
remote_id int not null,
description longtext,
created datetime,
processed int default 0,
processing_date datetime,
user_id int not null,
constraint user_fk foreign key (user_id) references users(id));

create table skup_reclassification_details (id int not null primary key auto_increment,
reclassification_id int not null,
barcode bigint(13) not null,
remote_rodzaj_id int not null,
picker_id int not null,
uniq_id int not null,
local_rodzaj_id int,
active int default 1);

alter table skup_reclassification_details add index reclassification_idx  (reclassification_id);
alter table skup_reclassification_details add index remote_rodzaj_idx (remote_rodzaj_id);
alter table skup_reclassification_details add foreign key reclassification_fk  (reclassification_id) references skup_reclassification (id);
alter table skup_reclassification_details add constraint foreign key rodzaj_fk949  (remote_rodzaj_id) references skup_rodzaj (remote_id);
alter table skup_reclassification_details add constraint foreign key local_rodzaj_fk (local_rodzaj_id) references rodzaj (id);


