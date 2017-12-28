package org.isf.exa.service;

import java.util.List;

import org.isf.exa.model.ExamRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ExamRowIoOperationRepository extends JpaRepository<ExamRow, Integer> {
   
    @Query(value = "SELECT * FROM EXAMROW ORDER BY EXR_EXA_ID_A, EXR_DESC", nativeQuery= true)
    public List<ExamRow> findAllByOrderIdAndDescriptionAsc();    
    @Query(value = "SELECT * FROM EXAMROW WHERE EXR_EXA_ID_A = :code ORDER BY EXR_EXA_ID_A, EXR_DESC", nativeQuery= true)
    public List<ExamRow> findAllWhereIdByOrderIdAndDescriptionAsc(@Param("code") String code);    
    @Query(value = "SELECT * FROM EXAMROW WHERE EXR_EXA_ID_A = :code AND EXR_DESC = :description ORDER BY EXR_EXA_ID_A, EXR_DESC", nativeQuery= true)
    public List<ExamRow> findAllWhereIdAndDescriptionByOrderIdAndDescriptionAsc(@Param("code") String code, @Param("description") String description);
    

    @Modifying 
    @Transactional
    @Query(value = "DELETE FROM EXAMROW WHERE EXR_EXA_ID_A = :code", nativeQuery= true)
    public void deleteWhereCode(@Param("code") String code);    
}