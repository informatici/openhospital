select 'OPD',dcl_desc, DIS_DESC from DISEASE, DISEASETYPE
where DIS_DCL_ID_A=dcl_id_a
and DIS_OPD_INCLUDE = 1 or DIS_OPD_INCLUDE is null
order by DCL_DESC, DIS_DESC
go
select 'IPD',dcl_desc, DIS_DESC from DISEASE, DISEASETYPE
where DIS_DCL_ID_A=dcl_id_a
and DIS_IPD_INCLUDE = 1 or DIS_IPD_INCLUDE is null
order by DCL_DESC, DIS_DESC
go
select dcl_desc, DIS_DESC ,
case when DIS_OPD_INCLUDE is null or DIS_OPD_INCLUDE =1 then 'OPD'  else '' end as OPD,
case when DIS_IPD_INCLUDE is null or DIS_IPD_INCLUDE =1 then 'IPD'  else '' end as IPD
from DISEASE, DISEASETYPE
where DIS_DCL_ID_A=dcl_id_a
order by DCL_DESC, DIS_DESC