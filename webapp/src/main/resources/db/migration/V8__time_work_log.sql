CREATE TABLE time_work_log (id int not null AUTO_INCREMENT PRIMARY KEY ,
user_id int not null,
rfid int not null,
start_time DATETIME,
end_time DATETIME,
FOREIGN KEY user_id_fk (user_id) REFERENCES ludzie(id),
FOREIGN KEY rfid_fk (rfid) REFERENCES ludzie(rfid));
