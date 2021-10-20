alter table skup_rodzaj add active tinyint after description;
update skup_rodzaj set active = 1;
