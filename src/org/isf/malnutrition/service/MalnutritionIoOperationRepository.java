package org.isf.malnutrition.service;

import java.util.List;

import org.isf.malnutrition.model.Malnutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MalnutritionIoOperationRepository extends JpaRepository<Malnutrition, Integer> {

    @Query(value = "SELECT * FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = :id ORDER BY MLN_DATE_SUPP", nativeQuery= true)
    List<Malnutrition> findAllWhereAdmissionByOrderDate(@Param("id") String id);
    
    @Query(value = "SELECT * FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = :id ORDER BY MLN_DATE_SUPP DESC LIMIT 1", nativeQuery= true)
    List<Malnutrition> findAllWhereAdmissionByOrderDateDescLimit1(@Param("id") int patientID);
    
}