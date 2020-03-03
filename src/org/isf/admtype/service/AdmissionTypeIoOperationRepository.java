package org.isf.admtype.service;

import java.util.List;

import org.isf.admtype.model.AdmissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionTypeIoOperationRepository extends JpaRepository<AdmissionType, String> {
    List<AdmissionType> findAllByOrderByDescriptionAsc();
}