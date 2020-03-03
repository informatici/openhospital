package org.isf.hospital.service;

import org.isf.hospital.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalIoOperationRepository extends JpaRepository<Hospital, String> {

    @Query(value = "SELECT HOS_CURR_COD FROM HOSPITAL", nativeQuery= true)
    String findHospitalCurrent();
    
}