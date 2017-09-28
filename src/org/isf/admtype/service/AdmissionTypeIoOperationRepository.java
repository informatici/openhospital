package org.isf.admtype.service;

import java.util.List;

import org.isf.admtype.model.AdmissionType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdmissionTypeIoOperationRepository extends JpaRepository<AdmissionType, String> {
    public List<AdmissionType> findAllOrderByDescriptionAsc();	
}