alter table hala add company_id int not null;
update hala set company_id = (select min(id) from company);
alter table hala add foreign key (company_id) references company(id);
