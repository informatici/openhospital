package org.isf.dlvrrestype.service;

import java.util.List;

import org.isf.dlvrrestype.model.DeliveryResultType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryResultIoOperationRepository extends JpaRepository<DeliveryResultType, String> {
    public List<DeliveryResultType> findAllByOrderByDescriptionAsc();
}