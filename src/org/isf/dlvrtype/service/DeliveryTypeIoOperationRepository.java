package org.isf.dlvrtype.service;

import org.isf.dlvrtype.model.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTypeIoOperationRepository extends JpaRepository<DeliveryType, String> {
	
}
