select 
    (case when OPD_NEW_PAT='N' then 'new attendance' else 'reattendance' end) as category, 
    (case when OPD_AGE <5 then '0-4 Years' else '5 and over' end) as AGE,
    opd_sex 'SEX',
    count(*) as COUNT
from OPD
where month(OPD_DATE_VIS) = 11 and year(OPD_DATE_VIS)=2006
group by case when OPD_NEW_PAT='N' then 'new attendance' else 'reattendance' end,
    (case when OPD_AGE <5 then '0-4 Years' else '5 and over' end),
    opd_sex
union
select 
    'total' as category, 
    (case when OPD_AGE <5 then '0-4 Years' else '5 and over' end) as AGE,
    opd_sex 'SEX',
    count(*) as COUNT
from OPD
where month(OPD_DATE_VIS) = 11 and year(OPD_DATE_VIS)=2006
group by 'total',
    (case when OPD_AGE <5 then '0-4 Years' else '5 and over' end),
    opd_sex
order by 1,2,3 desc

GO
