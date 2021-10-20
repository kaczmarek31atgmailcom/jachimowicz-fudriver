ALTER TABLE orders
  CHANGE plate truck_id INT;
ALTER TABLE orders
  ADD FOREIGN KEY (truck_id) REFERENCES truck (id);
