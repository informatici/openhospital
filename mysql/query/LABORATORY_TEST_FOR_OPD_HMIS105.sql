select EXA_DESC EXAM,'number of test done' category,lab_sex SEX ,  count(*) COUNT from LABORATORY, EXAM
where LAB_EXA_ID_A in ('03.01','05.05','05.07','06.1')
and LAB_EXA_ID_A = EXA_ID_A
and lab_pat_inout='O'
group by LAB_EXA_ID_A,lab_sex,'test done'
union
select EXA_DESC,case when (LAB_RES=EXA_DEFAULT) then 'Negative' else 'Positive' end,lab_sex,  count(*) from LABORATORY, EXAM
where LAB_EXA_ID_A in ('03.01','05.05','05.07','06.1')
and LAB_EXA_ID_A = EXA_ID_A
and lab_pat_inout='O'
group by LAB_EXA_ID_A,lab_sex,case when (LAB_RES=EXA_DEFAULT) then 'Negative' else 'Positive' end
union
select 'all other','test done',lab_sex, count(*) from LABORATORY, EXAM
where not LAB_EXA_ID_A in ('03.01','05.05','05.07','06.1')
and LAB_EXA_ID_A = EXA_ID_A
and lab_pat_inout='O'
group by 'all other',lab_sex,'test done'
union
select 'all other',case when (LAB_RES=EXA_DEFAULT) then 'Negative' else 'Positive' end,lab_sex, count(*) from LABORATORY, EXAM
where not LAB_EXA_ID_A in ('03.01','05.05','05.07','06.1')
and LAB_EXA_ID_A = EXA_ID_A
and lab_pat_inout='O'
group by 'all other',lab_sex,case when (LAB_RES=EXA_DEFAULT) then 'Negative' else 'Positive' end

