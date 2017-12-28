package org.isf.lab.service;

import org.isf.lab.model.LaboratoryRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface LabRowIoOperationRepository extends JpaRepository<LaboratoryRow, Integer> {

	@Modifying
    @Query(value = "DELETE FROM LABORATORYROW WHERE LABR_LAB_ID = :id", nativeQuery= true)
	@Transactional
	public void deleteWhereLab(@Param("id") Integer id);
        
}