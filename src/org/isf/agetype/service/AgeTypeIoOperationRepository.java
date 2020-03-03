package org.isf.agetype.service;

import java.util.List;

import org.isf.agetype.model.AgeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeTypeIoOperationRepository extends JpaRepository<AgeType, String> {
    List<AgeType> findAllByOrderByCodeAsc();
    AgeType findOneByCode(String code);
}