set foreign_key_checks  = 0;
rename table east_mushrooms_wz_palette to east_mushrooms_shipment_palette;
CREATE TABLE east_mushrooms_shipment
(
    id              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uuid            VARCHAR(60),
    monthly_no      INT,
    creation_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_id int not null,
    customer_name varchar(60) CHARACTER SET utf8mb4 DEFAULT NULL,
    customer_address varchar(60) CHARACTER SET utf8mb4 DEFAULT NULL,
    creator_id      INT,
    creator_login   varchar(60) CHARACTER SET utf8mb4,
    creator_name    varchar(60) CHARACTER SET utf8mb4,
    creator_surname varchar(60) CHARACTER SET utf8mb4,
    FOREIGN KEY (creator_id) references users (id),
    FOREIGN KEY (customer_id) references skrz_cust(id)
);

DELIMITER $$
CREATE TRIGGER EAST_MUSHROOMS_SHIPMENT_MONTHLY_NUMBER_TRIGGER
    BEFORE INSERT
    ON east_mushrooms_shipment
    FOR EACH ROW
BEGIN
    SET NEW.monthly_no = (
        SELECT IFNULL(MAX(monthly_no), 0) + 1
        FROM east_mushrooms_shipment
        WHERE DATE_FORMAT(creation_date, '%Y%m') = DATE_FORMAT(curdate(), '%Y%m')
    );
END $$
DELIMITER ;

alter table east_mushrooms_shipment_palette change wz_document_id shipment_id int not null;
alter table east_mushrooms_shipment_palette drop foreign key east_mushrooms_shipment_palette_ibfk_1;
alter table east_mushrooms_shipment_palette drop foreign key east_mushrooms_shipment_palette_ibfk_2;
alter table east_mushrooms_shipment_palette add foreign key (shipment_id) references east_mushrooms_shipment(id);

alter table east_mushrooms_wz_unit change wz_palette_id shipment_palette_id int not null;
alter table east_mushrooms_wz_unit drop foreign key east_mushrooms_wz_unit_ibfk_1;
alter table east_mushrooms_wz_unit add foreign key (shipment_palette_id) references east_mushrooms_shipment_palette(id);
alter table east_mushrooms_wz_unit add company_id int not null;
alter table east_mushrooms_wz_unit add foreign key (company_id) references company(id);
alter table east_mushrooms_wz_unit add wz_id int not null;
alter table east_mushrooms_wz_unit add foreign key (wz_id) references east_mushrooms_wz_document(id);

alter table east_mushrooms_wz_document add company_id int not null after monthly_no;
alter table east_mushrooms_wz_document add foreign key (company_id) references company(id);

alter table east_mushrooms_wz_document add column shipment_id int default null after company_id;
alter table east_mushrooms_wz_document add foreign key (shipment_id) references east_mushrooms_shipment(id);

create index creation_date on east_mushrooms_shipment(creation_date);
create index wzDocument_palette on east_mushrooms_wz_unit(wz_id,shipment_palette_id);
create index wz_id on east_mushrooms_wz_unit (wz_id);
alter table east_mushrooms_wz_unit add constraint foreign key (local_type_id) references rodzaj(id);
set foreign_key_checks  = 1;
