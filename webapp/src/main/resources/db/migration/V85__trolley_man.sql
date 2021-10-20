create table trolley_man(
                            id int not null auto_increment primary key,
                            name varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                            surname varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                            active TINYINT
);
alter table zarobki add foreign key wozkowy_id(wozkowy_id) references trolley_man(id);
alter table east_mushrooms_warehouse_unit add trolley_man_id int after scanner_man_id;
alter table east_mushrooms_warehouse_unit add foreign key trolley_man_id(trolley_man_id) references trolley_man(id);

