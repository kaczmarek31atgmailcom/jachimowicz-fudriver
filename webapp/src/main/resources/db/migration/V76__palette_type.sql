create table palette_type
(
    id                INT     NOT NULL PRIMARY KEY AUTO_INCREMENT,
    remote_palette_id int     not null,
    name              varchar(60) CHARACTER SET utf8mb4 DEFAULT NULL,
    active            tinyint not null,
    unique remote_palette_id (remote_palette_id)
);
