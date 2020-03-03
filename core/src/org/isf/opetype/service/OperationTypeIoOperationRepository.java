package org.isf.opetype.service;

import java.util.List;

import org.isf.opetype.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeIoOperationRepository extends JpaRepository<OperationType, String> {
    List<OperationType> findAllByOrderByDescriptionAsc();
}