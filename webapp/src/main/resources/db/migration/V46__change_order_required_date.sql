ALTER TABLE orders
  CHANGE required_date required_date DATE;
ALTER TABLE delivery_letter_palette
  DROP INDEX delivery_letter_position;