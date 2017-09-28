package org.isf.exatype.service;

import java.util.List;

import org.isf.exatype.model.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExamTypeIoOperationRepository extends JpaRepository<ExamType, String> {
    public List<ExamType> findAllOrderByDescriptionAsc();
}