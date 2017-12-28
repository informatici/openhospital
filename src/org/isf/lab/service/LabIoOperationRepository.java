package org.isf.lab.service;

import java.util.GregorianCalendar;
import java.util.List;

import org.isf.lab.model.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LabIoOperationRepository extends JpaRepository<Laboratory, Integer> {

    @Query(value = "SELECT * FROM LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A "
    		+ "WHERE LAB_EXAM_DATE >= :dateFrom AND LAB_EXAM_DATE <= :dateTo ORDER BY LAB_EXAM_DATE", nativeQuery= true)
    public List<Laboratory> findAllWhereDatesByOrderExamDate(
    		@Param("dateFrom") GregorianCalendar dateFrom,
    		@Param("dateFrom") GregorianCalendar dateTo);
    @Query(value = "SELECT * FROM LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A "
    		+ "WHERE LAB_EXAM_DATE >= :dateFrom AND LAB_EXAM_DATE <= :dateTo AND EXA_DESC = :exam ORDER BY LAB_EXAM_DATE", nativeQuery= true)
    public List<Laboratory> findAllWhereDatesAndExamByOrderExamDate(
    		@Param("dateFrom") GregorianCalendar dateFrom,
    		@Param("dateTo") GregorianCalendar dateTo,
    		@Param("exam") String exam);
    
    @Query(value = "SELECT * FROM (LABORATORY JOIN EXAM ON LAB_EXA_ID_A=EXA_ID_A)" +
    		 " LEFT JOIN LABORATORYROW ON LABR_LAB_ID = LAB_ID WHERE LAB_PAT_ID = :patient " +
    		 " ORDER BY LAB_DATE, LAB_ID", nativeQuery= true)
    public List<Laboratory> findAllWherePatientByOrderDateAndId(
    		@Param("patient") Integer patient);
    
    
    @Query(value = "SELECT * FROM (LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A)" +
		       " JOIN EXAMTYPE ON EXC_ID_A = EXA_EXC_ID_A" +
			   " WHERE LAB_DATE >= :dateFrom AND LAB_DATE <= :dateTo ORDER BY EXC_DESC", nativeQuery= true)
    public List<Laboratory> findAllWhereDatesForPrint(
    		@Param("dateFrom") GregorianCalendar dateFrom,
    		@Param("dateFrom") GregorianCalendar dateTo);
    @Query(value = "SELECT * FROM (LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A)" +
		       " JOIN EXAMTYPE ON EXC_ID_A = EXA_EXC_ID_A" +
			   " WHERE LAB_DATE >= :dateFrom AND LAB_DATE <= :dateTo AND EXA_DESC LIKE %:exam% ORDER BY EXC_DESC", nativeQuery= true)
    public List<Laboratory> findAllWhereDatesAndExamForPrint(
    		@Param("dateFrom") GregorianCalendar dateFrom,
    		@Param("dateTo") GregorianCalendar dateTo,
    		@Param("exam") String exam);
   
}