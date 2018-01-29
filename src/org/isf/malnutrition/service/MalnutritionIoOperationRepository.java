package org.isf.malnutrition.service;

import java.util.List;

import org.isf.malnutrition.model.Malnutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MalnutritionIoOperationRepository extends JpaRepository<Malnutrition, Integer> {

    @Query(value = "SELECT * FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = :id ORDER BY MLN_DATE_SUPP", nativeQuery= true)
    public List<Malnutrition> findAllWhereAdmissionByOrderDate(@Param("id") String id);
    
    @Query(value = "SELECT * FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = :id ORDER BY MLN_DATE_SUPP DESC LIMIT 1", nativeQuery= true)
    public List<Malnutrition> findAllWhereAdmissionByOrderDateLimit1(@Param("id") int patientID);
    
}