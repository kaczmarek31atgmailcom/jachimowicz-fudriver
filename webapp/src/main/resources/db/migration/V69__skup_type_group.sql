alter table skup_rodzaj add group_id int after weight;
alter table skup_rodzaj add group_name varchar(30) CHARACTER SET utf8mb4 after group_id;
