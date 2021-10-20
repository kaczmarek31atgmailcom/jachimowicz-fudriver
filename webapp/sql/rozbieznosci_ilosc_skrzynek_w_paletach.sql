insert into zarobki (ludzie_id,export,kraj,inne,hala_id ,time,rodzaj_id,zaplacono,wozek_nr,timeshort,user_id,uniq_id,ilosc)
select
    wz.picker_id,
    round(r.waga * (r.export = 2),3),
    round(r.waga * (r.export = 1)),
    round(r.waga * (r.export = 3)),
    wz.cycle_id,
    wz.harvest_date,
    r.id,
    0,
    wu.harvest_palette_id,
    DATE_FORMAT(wz.harvest_date,'%Y%m'),
    wu.scanner_man_id,
    wz.uniq_id,
    r.waga
from east_mushrooms_wz_unit wz
         left join zarobki z on z.uniq_id = wz.uniq_id and z.ludzie_id = wz.picker_id
         left join rodzaj r on r.id = wz.local_type_id
         left join east_mushrooms_warehouse_unit wu on wu.picker_id = wz.picker_id and wu.uniq_id = wz.uniq_id
where z.id is null
  and wz.harvest_date >= '2020-04-01'
  and wz.picker_id <> 1680;


insert into zarobki (ludzie_id,export,kraj,inne,hala_id ,time,rodzaj_id,zaplacono,wozek_nr,timeshort,user_id,uniq_id,ilosc)
select
    wz.picker_id,
    round(r.waga * (r.export = 2),3),
    round(r.waga * (r.export = 1)),
    round(r.waga * (r.export = 3)),
    wz.cycle_id,
    wz.harvest_date,
    r.id,
    0,
    wu.harvest_palette_id,
    DATE_FORMAT(wz.harvest_date,'%Y%m'),
    wu.scanner_man_id,
    wz.uniq_id,
    r.waga
from east_mushrooms_wz_unit wz
         left join zarobki z on z.uniq_id = wz.uniq_id and z.ludzie_id = wz.picker_id
         left join rodzaj r on r.id = wz.local_type_id
         left join east_mushrooms_warehouse_unit wu on wu.picker_id = wz.picker_id and wu.uniq_id = wz.uniq_id
where z.id is null
  and wz.harvest_date >= '2020-04-25'
  and wz.picker_id = 1680;

$$$$$$$$$$$$$$$$$$$$$$
update wozek set user_id = 72 where 1=1;

select wozek_nr,count(*) from wozek group by wozek_nr;
select count(*) from wozek where wozek_nr = 173146;

select count(*) from zarobki where wozek_nr = 173146;

select count(*) from east_mushrooms_wz_unit wz
                         left join zarobki z on z.uniq_id = wz.uniq_id and z.ludzie_id = wz.picker_id
                         left join rodzaj r on r.id = wz.local_type_id
                         left join east_mushrooms_warehouse_unit wu on wu.picker_id = wz.picker_id and wu.uniq_id = wz.uniq_id
where z.id is null
  and wz.harvest_date >= '2020-04-01'
  and wz.picker_id <> 1680;


select u.harvest_palette_id,u.harvest_time, count(u.id), count(z.id)
from east_mushrooms_warehouse_unit u
         left join zarobki z on z.wozek_nr = u.harvest_palette_id
group by u.harvest_palette_id
having count(u.id) <> count(z.id);
