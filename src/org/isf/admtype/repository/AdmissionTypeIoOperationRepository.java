package org.isf.admtype.repository;

import java.util.List;

import org.isf.admtype.model.AdmissionType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdmissionTypeIoOperationRepository extends JpaRepository<AdmissionType, String> {
    public List<AdmissionType> findAllByOrderByDescriptionAsc();	
}