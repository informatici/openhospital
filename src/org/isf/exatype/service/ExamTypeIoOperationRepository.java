package org.isf.exatype.service;

import java.util.List;

import org.isf.exatype.model.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamTypeIoOperationRepository extends JpaRepository<ExamType, String> {
    List<ExamType> findAllByOrderByDescriptionAsc();
}