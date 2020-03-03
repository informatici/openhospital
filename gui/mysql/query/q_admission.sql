
select PATIENT.*,g.pregnancy,g.normal
from PATIENT join 

(select DISTINCTROW PAT_ID , sum(pregnancy) as pregnancy,
 sum(normal) as normal
from PATIENT 
left join

(select DISTINCTROW ADM_PAT_ID , case 
when (ADM_TYPE = 'P' and ADM_IN = 1) then 1 
else 0
end as 'pregnancy'
from ADMISSION 
where ADM_DELETED <> 'Y') tp

on PAT_ID = tp.ADM_PAT_ID 

left join

(select DISTINCTROW  ADM_PAT_ID , case 
when (ADM_TYPE = 'N' and ADM_IN = 1) then 1
else 0
end as 'normal'
from ADMISSION 
where ADM_DELETED <> 'Y' ) tn

on PAT_ID = tn.ADM_PAT_ID 

where PAT_SEX = 'F'

group by PAT_ID) g

on PATIENT.PAT_ID = g.PAT_ID 
;