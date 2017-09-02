package org.isf.dlvrtype.repository;

import org.isf.dlvrtype.model.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryTypeIoOperationRepository extends JpaRepository<DeliveryType, String> {
}