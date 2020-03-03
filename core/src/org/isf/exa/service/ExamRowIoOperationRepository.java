package org.isf.exa.service;

import java.util.List;

import org.isf.exa.model.ExamRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ExamRowIoOperationRepository extends JpaRepository<ExamRow, Integer> {
    
    @Query(value = "SELECT * FROM EXAMROW", nativeQuery= true)// ORDER BY EXR_ID, EXR_DESC", nativeQuery= true)
    List<ExamRow> findAllExamRow();
    
    @Query(value = "SELECT * FROM EXAMROW WHERE EXR_ID = :code ORDER BY EXR_ID, EXR_DESC", nativeQuery= true)
    List<ExamRow> findAllWhereIdByOrderIdAndDescriptionAsc(@Param("code") int code);
    
    @Query(value = "SELECT * FROM EXAMROW WHERE EXR_DESC = :description ORDER BY EXR_DESC", nativeQuery= true)
    List<ExamRow> findAllWhereDescriptionByOrderDescriptionAsc(@Param("description") String description);
    
    @Query(value = "SELECT * FROM EXAMROW WHERE EXR_ID = :code AND EXR_DESC = :description ORDER BY EXR_ID, EXR_DESC", nativeQuery= true)
    List<ExamRow> findAllWhereIdAndDescriptionByOrderIdAndDescriptionAsc(@Param("code") int code, @Param("description") String description);
    
    @Modifying 
    @Transactional
    @Query(value = "DELETE FROM EXAMROW WHERE EXR_EXA_ID_A = :code", nativeQuery= true)
    void deleteExamRowWhereExamCode(@Param("code") String code);
    
    @Query(value = "SELECT * FROM EXAMROW WHERE EXR_EXA_ID_A = :examCode ORDER BY EXR_DESC", nativeQuery= true)
    List<ExamRow> getExamRowByExamCode(@Param("examCode") String examCode);

}