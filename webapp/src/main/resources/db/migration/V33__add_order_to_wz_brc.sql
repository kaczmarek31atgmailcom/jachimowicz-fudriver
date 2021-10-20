SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE wz_brc_header ADD order_id INT NOT NULL;
ALTER TABLE wz_brc_header ADD FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE wz_brc_detail ADD palette_id INT NOT NULL;
ALTER TABLE wz_brc_detail ADD FOREIGN KEY (palette_id) REFERENCES palette(id);
SET FOREIGN_KEY_CHECKS = 1;