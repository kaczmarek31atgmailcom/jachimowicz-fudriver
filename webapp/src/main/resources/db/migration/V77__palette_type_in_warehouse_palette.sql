set foreign_key_checks =0;
alter table east_mushrooms_warehouse_palette add palette_type_id int not null;
alter table east_mushrooms_warehouse_palette add foreign key (palette_type_id) references palette_type(id);
alter table east_mushrooms_shipment_palette add palette_type_id int not null after warehouse_palette_id;
alter table east_mushrooms_shipment_palette add foreign key (palette_type_id) references palette_type(id);
set foreign_key_checks =1;
