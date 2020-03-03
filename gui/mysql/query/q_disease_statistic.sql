Select dt.DCL_DESC,d.DIS_DESC,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE<5
    and p1.PAT_SEX = 'M'
    and (a1.ADM_DIST_ID_A<>'D' or a1.ADM_DIST_ID_A is null) ) AliveMaleUnder5,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE<5
    and p1.PAT_SEX = 'M'
    and a1.ADM_DIST_ID_A='D') DeadMaleUnder5,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE<5
    and p1.PAT_SEX = 'F'
    and (a1.ADM_DIST_ID_A<>'D' or a1.ADM_DIST_ID_A is null) ) AliveFemaleUnder5,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE<5
    and p1.PAT_SEX = 'F'
    and a1.ADM_DIST_ID_A='D') DeadFemaleUnder5,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE>=5
    and p1.PAT_SEX = 'M'
    and (a1.ADM_DIST_ID_A<>'D' or a1.ADM_DIST_ID_A is null) ) AliveMaleAdult,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE>=5
    and p1.PAT_SEX = 'M'
    and a1.ADM_DIST_ID_A='D') DeadMaleAdult,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE>=5
    and p1.PAT_SEX = 'F'
    and (a1.ADM_DIST_ID_A<>'D' or a1.ADM_DIST_ID_A is null) ) AliveFemaleAdult,
   (select count(*) from ADMISSION a1, PATIENT p1
    where a1.ADM_IN_DIS_ID_A = d.dis_id_a
    and a1.ADM_PAT_ID=p1.PAT_ID
    and month(ADM_DATE_ADM)=11
    and year(ADM_DATE_ADM)=2006
    and a1.ADM_DELETED=0
    and p1.PAT_AGE>=5
    and p1.PAT_SEX = 'F'
    and a1.ADM_DIST_ID_A='D') DeadFemaleAdult
    FROM DISEASE d, DISEASETYPE dt
where d.DIS_DCL_ID_A = dt.DCL_ID_A
order by dt.DCL_DESC,d.DIS_DESC

go
SELECT dt.DCL_DESC,ADM_IN_DIS_ID_A, DIS_DESC, CASE WHEN PAT_AGE<5 THEN "1-4" ELSE ">5" END AGE_BAND, count(*)
    FROM ADMISSION a, DISEASE d, PATIENT p, DISEASETYPE dt
where a.ADM_IN_DIS_ID_A = d.DIS_ID_A
and a.ADM_PAT_ID=p.PAT_ID
and d.DIS_DCL_ID_A = dt.DCL_ID_A
and month(ADM_DATE_ADM)=11
and year(ADM_DATE_ADM)=2006
and a.ADM_DELETED=0
group by dt.DCL_DESC,ADM_IN_DIS_ID_A, DIS_DESC, CASE WHEN PAT_AGE<5 THEN "1-4" ELSE ">5" END 
