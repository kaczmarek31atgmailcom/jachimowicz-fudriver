alter table ludzie add (data_urodzenia int(8), miasto varchar(40), obwod varchar(40),rejon varchar(40),indeks varchar(40),imiona_rodzicow varchar(50), nr_wizy varchar(40), data_waznosci_zezwolenia int(8),pesel varchar(30), czy_zbieraczka int default 0, czy_akord int default 0, numer_szafki_stolowka int, numer_szafki_szatnia int, numer_wagi int, rfid int,active int default 1);
update ludzie set grupa = 1 where grupa = 0;
update ludzie set grupa = 1 where grupa is null;
update ludzie set active = 0 where nr = 0;
alter table ludzie change grupa grupa int not null default 1;




