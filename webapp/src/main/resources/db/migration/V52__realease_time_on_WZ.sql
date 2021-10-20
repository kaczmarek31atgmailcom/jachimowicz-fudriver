ALTER TABLE palette
  ADD release_time DATETIME;
ALTER TABLE wz_brc_header
  ADD start_release_time TIMESTAMP NOT NULL;
ALTER TABLE wz_brc_header
  ADD end_release_time TIMESTAMP NOT NULL;

UPDATE wz_brc_header
SET start_release_time = time, end_release_time = time;