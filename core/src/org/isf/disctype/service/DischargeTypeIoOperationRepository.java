package org.isf.disctype.service;

import java.util.List;

import org.isf.disctype.model.DischargeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DischargeTypeIoOperationRepository extends JpaRepository<DischargeType, String> {
    List<DischargeType> findAllByOrderByDescriptionAsc();
}