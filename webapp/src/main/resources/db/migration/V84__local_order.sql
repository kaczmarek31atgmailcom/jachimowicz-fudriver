create table production_order_local(
    id int not null primary key auto_increment,
    type_id int not null,
    amount int default 0,
    due_date date,
    foreign key (type_id) references rodzaj(id),
    unique key (type_id,due_date)
);
