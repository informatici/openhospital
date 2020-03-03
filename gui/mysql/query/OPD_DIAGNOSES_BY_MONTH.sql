SELECT DCL_DESC as DiagType,
        DIS_DESC as Diagnosis,
        (case when OPD_AGE <5 then '0-4 Years' else '5 and over' end) as AGE,
        OPD_SEX, 
        count(*) as COUNT    
        FROM (
            select OPD_DIS_ID_A, OPD_AGE, OPD_SEX FROM OPD
            where OPD_NEW_PAT = 'N'
            and month(OPD_DATE_VIS) = 11 and year(OPD_DATE_VIS)=2006
            union all 
            select OPD_DIS_ID_A_2 as OPD_DIS_ID_A, OPD_AGE, OPD_SEX FROM OPD
            where not OPD_DIS_ID_A_2 is null
            and OPD_NEW_PAT = 'N'
            and month(OPD_DATE_VIS) = 11 and year(OPD_DATE_VIS)=2006
            union all 
            select OPD_DIS_ID_A_3 as OPD_DIS_ID_A, OPD_AGE, OPD_SEX FROM OPD
            where not OPD_DIS_ID_A_3 is null
            and OPD_NEW_PAT = 'N'
            and month(OPD_DATE_VIS) = 11 and year(OPD_DATE_VIS)=2006
            ) as OPDALLDIAG
            , DISEASE , DISEASETYPE
where  OPDALLDIAG.OPD_DIS_ID_A = DIS_ID_A
and DIS_DCL_ID_A = DCL_ID_A
group by
DCL_DESC ,
DIS_DESC ,
(case when OPD_AGE <5 then '0-4 Years' else '5 and over' end) ,
OPD_SEX 
order by 1,2,3,4 desc
GO
