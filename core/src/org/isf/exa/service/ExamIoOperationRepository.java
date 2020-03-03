package org.isf.exa.service;

import java.util.List;

import org.isf.exa.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamIoOperationRepository extends JpaRepository<Exam, String> {
  
    @Query(value = "SELECT EXA_ID_A FROM EXAM JOIN EXAMTYPE ON EXA_EXC_ID_A = EXC_ID_A ORDER BY EXC_DESC, EXA_DESC", nativeQuery= true)
    List<String> findAllByOrderDescriptionAsc();
    @Query(value = "SELECT EXA_ID_A FROM EXAM JOIN EXAMTYPE ON EXA_EXC_ID_A = EXC_ID_A WHERE EXC_DESC LIKE %:description% ORDER BY EXC_DESC, EXA_DESC", nativeQuery= true)
    List<String> findAllWhereDescriptionByOrderDescriptionAsc(@Param("description") String description);
}