package org.isf.lab.service;

import java.util.ArrayList;

import org.isf.lab.model.LaboratoryRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LabRowIoOperationRepository extends JpaRepository<LaboratoryRow, Integer> {

	@Modifying
	@Transactional
    @Query(value = "DELETE FROM LABORATORYROW WHERE LABR_LAB_ID = :id", nativeQuery= true)
    void deleteWhereLab(@Param("id") Integer id);
	
	@Modifying
	@Transactional
    @Query(value = "SELECT * FROM LABORATORYROW WHERE LABR_LAB_ID = :id", nativeQuery= true)
    ArrayList<LaboratoryRow> findAllByLaboratoryCode(@Param("id") Integer id);
        
}