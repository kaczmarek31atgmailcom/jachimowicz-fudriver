alter table cykle add zalozenie int after hala_id;
update cykle set zalozenie = DATE_FORMAT(poczatek - INTERVAL 14 DAY,'%Y%m%d');
