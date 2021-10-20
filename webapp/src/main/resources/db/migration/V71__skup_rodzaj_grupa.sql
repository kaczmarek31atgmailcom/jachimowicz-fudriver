create table skup_rodzaj_grupa
(
    id         int not null primary key auto_increment,
    group_id   int not null,
    group_name varchar(30) CHARACTER SET utf8mb4,
    active     tinyint default 1
);

create table harvest_estimation
(
    id                int not null auto_increment primary key,
    cycle_id          int not null,
    day_number        int not null,
    group_id          int not null,
    calculated_amount int,
    estimated_amount  int,
    user_id           int,
    estimation_date   date,
    foreign key (cycle_id) references cykle (id),
    foreign key (group_id) references skup_rodzaj_grupa (id),
    foreign key (user_id) references users (id)
);
