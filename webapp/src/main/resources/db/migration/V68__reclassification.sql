create table east_mushrooms_reclassification_header
(
  id           int not null primary key auto_increment,
  uuid         VARCHAR(60) DEFAULT NULL,
  date         DATE,
  user_id      int not null,
  login        varchar(60) CHARACTER SET utf8mb4,
  user_name    varchar(60) CHARACTER SET utf8mb4,
  user_surname varchar(60) CHARACTER SET utf8mb4,
  foreign key (user_id) references users (id)
);

create table east_mushrooms_reclassification_detail
(
  id                 int    not null primary key auto_increment,
  uuid               VARCHAR(60) DEFAULT NULL,
  header_id          int    not null,
  picker_id          int    not null,
  uniq_id            int    not null,
  harvest_date       date,
  source_type_id     int    not null,
  source_type_name   varchar(100) CHARACTER SET utf8mb4,
  source_type_weight double not null,
  target_type_id     int    not null,
  target_type_name   varchar(100) CHARACTER SET utf8mb4,
  target_type_weight double not null,
  foreign key (header_id) references east_mushrooms_reclassification_header (id)
);
