set foreign_key_checks =0;
drop table delivery_letter;
drop table delivery_letter_palette;
drop table batch;
drop table orders;
drop table palette;
drop table truck;
drop table warehouse_brc;
drop table chlodnia;
drop table wz_brc_detail;
drop table wz_brc_header;
drop table wz_brc_palette;
set foreign_key_checks =1;

create table production_order
(
    id                 int     not null primary key auto_increment,
    warehouse_order_id int     not null,
    creation_date      date    not null,
    due_date           date    not null,
    warehouse_type_id  int     not null,
    due_amount         int     not null,
    delivered_amount   int     not null                    default 0,
    status             tinyint not null,
    description        VARCHAR(3000) CHARACTER SET utf8mb4 DEFAULT NULL,
    version            int                                 default 0
);
