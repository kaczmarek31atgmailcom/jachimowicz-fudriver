create table east_mushrooms_shipment_order(
    id int not null primary key,
    shipment_id int not null,
    remote_type_id int not null,
    remote_type_name VARCHAR(3000) CHARACTER SET utf8mb4 DEFAULT NULL,
    remote_type_weight int,
    local_type_id int not null,
    local_type_name VARCHAR(3000) CHARACTER SET utf8mb4 DEFAULT NULL,
    local_type_weight int,
    amount int,
    ordered_amount int,
    delivered_amount int,
    foreign key (shipment_id) references east_mushrooms_shipment(id)
);
