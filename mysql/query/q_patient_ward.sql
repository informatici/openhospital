select PATIENT.* , final.WARD 
from PATIENT join 
(select g.ID, max(g.WARD) as 'WARD' from
(select DISTINCTROW PATIENT.pat_id as 'ID', 
case 
when ( ADM_IN = 1) then WARD.WRD_ID_A 
else NULL
end as 'WARD'

from  PATIENT
left join
ADMISSION on PAT_ID = ADM_PAT_ID
left join 
WARD on ADM_WRD_ID_A = WRD_ID_A
where ADMISSION.ADM_DELETED ='N' or ADMISSION.ADM_DELETED is null
order by PATIENT.pat_name) g
group by g.ID) final
on PAT_ID = ID