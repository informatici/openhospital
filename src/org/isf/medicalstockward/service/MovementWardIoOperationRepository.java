package org.isf.medicalstockward.service;

import org.isf.medicalstockward.model.MovementWard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovementWardIoOperationRepository extends JpaRepository<MovementWard, Integer>{      
	   
}
