select * from (
select EXA_DESC EXAM,LAB_MATERIAL,'tests done' category,  count(*) COUNT from LABORATORY, EXAM
where LAB_EXA_ID_A = EXA_ID_A
and month(LAB_EXAM_DATE)=11 and year(LAB_EXAM_DATE)=2006
group by EXA_DESC,LAB_MATERIAL,'tests done'
union all
select EXA_DESC,LAB_MATERIAL,case when (LAB_RES=EXA_DEFAULT) then 'Negative' else 'Positive' end,  count(*) from LABORATORY, EXAM
where LAB_EXA_ID_A = EXA_ID_A
and month(LAB_EXAM_DATE)=11 and year(LAB_EXAM_DATE)=2006
group by EXA_DESC,LAB_MATERIAL,case when (LAB_RES=EXA_DEFAULT) then 'Negative' else 'Positive' end
union
select EXA_DESC,'xall materials','total tests done' category,  count(*) COUNT from LABORATORY, EXAM
where LAB_EXA_ID_A = EXA_ID_A
and month(LAB_EXAM_DATE)=11 and year(LAB_EXAM_DATE)=2006
group by EXA_DESC,'xall materials','total tests done'
) labdata
 order by 1,2,3
