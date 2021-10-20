alter table ludzie add column version int;
alter table ludzie change imie imie varchar(30) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change nazwisko nazwisko varchar(30) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change adres adres varchar(40) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change nr_telefonu nr_telefonu varchar(30) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change password password text character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change imiona_rodzicow imiona_rodzicow varchar(50) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change indeks indeks varchar(40) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change miasto miasto varchar(40) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change nr_wizy nr_wizy varchar(40) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change obwod obwod varchar(40) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change pesel pesel varchar(30) character set utf8mb4 collate utf8mb4_unicode_ci;
alter table ludzie change rejon rejon varchar(40) character set utf8mb4 collate utf8mb4_unicode_ci;

