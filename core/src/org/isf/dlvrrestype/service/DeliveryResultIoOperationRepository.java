package org.isf.dlvrrestype.service;

import java.util.List;

import org.isf.dlvrrestype.model.DeliveryResultType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryResultIoOperationRepository extends JpaRepository<DeliveryResultType, String> {
    List<DeliveryResultType> findAllByOrderByDescriptionAsc();
}