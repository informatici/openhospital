package org.isf.exatype.repository;

import java.util.List;

import org.isf.exatype.model.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExamTypeIoOperationRepository extends JpaRepository<ExamType, String> {
    public List<ExamType> findAllByOrderByDescriptionAsc();
}