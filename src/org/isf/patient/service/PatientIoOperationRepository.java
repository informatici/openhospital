package org.isf.patient.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.isf.patient.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface PatientIoOperationRepository extends JpaRepository<Patient, Integer>, PatientIoOperationRepositoryCustom {
    
	@Query(value = "SELECT * FROM PATIENT WHERE (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_ID", nativeQuery= true)
    List<Patient> findAllWhereDeleted();

	
	@Query(value = "SELECT * FROM PATIENT WHERE (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_NAME", nativeQuery= true)
    List<Patient> findAllWhereDeletedOrderedByName();

    @Query(value = "SELECT * FROM PATIENT WHERE (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_NAME, ?#{#pageable}", nativeQuery= true)
    List<Patient> findAllWhereDeleted(Pageable pageable);

    List<Patient> findAllByDeletedIsNullOrDeletedEqualsOrderByName(String patDeleted, Pageable pageable);
    
    @Query(value = "SELECT * FROM PATIENT WHERE PAT_NAME = :name AND (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_SNAME,PAT_FNAME", nativeQuery= true)
    List<Patient> findAllWhereNameAndDeletedOrderedByName(@Param("name") String name);

    @Query(value = "SELECT * FROM PATIENT WHERE PAT_ID = :id AND (PAT_DELETED='N' OR PAT_DELETED IS NULL)", nativeQuery= true)
    List<Patient> findAllWhereIdAndDeleted(@Param("id") Integer id);
    
    @Query(value = "SELECT * FROM PATIENT WHERE PAT_ID = :id", nativeQuery= true)
    List<Patient> findAllWhereId(@Param("id") Integer id);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE PATIENT SET PAT_DELETED = 'Y' WHERE PAT_ID = :id", nativeQuery = true)
    int updateDeleted(@Param("id") Integer id);
            
    @Query(value = "SELECT * FROM PATIENT WHERE PAT_NAME = :name AND PAT_DELETED='N'", nativeQuery= true)
    List<Patient> findAllWhereName(@Param("name") String name);

    @Query(value = "SELECT MAX(PAT_ID) FROM PATIENT", nativeQuery= true)
    Integer findMaxCode();
        
    @Modifying
    @Transactional
    @Query(value = "UPDATE ADMISSION SET ADM_PAT_ID = :new_id WHERE ADM_PAT_ID = :old_id", nativeQuery= true)
    int updateAdmission(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PATIENTEXAMINATION SET PEX_PAT_ID = :new_id WHERE PEX_PAT_ID = :old_id", nativeQuery= true)
    int updateExamination(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE LABORATORY SET LAB_PAT_ID = :new_id, LAB_PAT_NAME = :name, LAB_AGE = :age, LAB_SEX = :sex WHERE LAB_PAT_ID = :old_id", nativeQuery= true)
    int updateLaboratory(@Param("new_id") Integer new_id, @Param("name") String name, @Param("age") Integer age, @Param("sex") String sex, @Param("old_id") Integer old_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE OPD SET OPD_PAT_ID = :new_id, OPD_AGE = :age, OPD_SEX = :sex WHERE OPD_PAT_ID = :old_id", nativeQuery= true)
    int updateOpd(@Param("new_id") Integer new_id, @Param("age") Integer age, @Param("sex") String sex, @Param("old_id") Integer old_id);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE BILLS SET BLL_ID_PAT = :new_id, BLL_PAT_NAME = :name WHERE BLL_ID_PAT = :old_id", nativeQuery= true)
    int updateBill(@Param("new_id") Integer new_id, @Param("name") String name, @Param("old_id") Integer old_id);
	
    @Modifying
    @Transactional
    @Query(value = "UPDATE MEDICALDSRSTOCKMOVWARD SET MMVN_PAT_ID = :new_id WHERE MMVN_PAT_ID = :old_id", nativeQuery= true)
    int updateMedicalStock(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE THERAPIES SET THR_PAT_ID = :new_id WHERE THR_PAT_ID = :old_id", nativeQuery= true)
    int updateTherapy(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);
	
    @Modifying
    @Transactional
    @Query(value = "UPDATE VISITS SET VST_PAT_ID = :new_id WHERE VST_PAT_ID = :old_id", nativeQuery= true)
    int updateVisit(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PATIENTVACCINE SET PAV_PAT_ID = :new_id WHERE PAV_PAT_ID = :old_id", nativeQuery= true)
    int updatePatientVaccine(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);
 		
    @Modifying
    @Transactional
    @Query(value = "UPDATE PATIENT SET PAT_DELETED = 'Y' WHERE PAT_ID = :id", nativeQuery= true)
    int updateDelete(@Param("id") Integer id); 		
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE PATIENT SET PAT_FNAME = :firstName, PAT_SNAME = :secondName, PAT_NAME  = :name, "
    		+ "PAT_BDATE = :bdate, PAT_AGE = :age, PAT_AGETYPE = :ageType, PAT_SEX = :sex, PAT_ADDR = :address, PAT_CITY = :city, "
    		+ "PAT_NEXT_KIN = :nextKin, PAT_TELE = :telephone, PAT_MOTH = :mother, PAT_MOTH_NAME = :motherName, "
    		+ "PAT_FATH = :father, PAT_FATH_NAME = :fatherName, PAT_BTYPE = :bType, PAT_ESTA = :esta, PAT_PTOGE = :ptoge, "
    		+ "PAT_NOTE = :note, PAT_TAXCODE = :taxCode, PAT_LOCK = :lock, PAT_PHOTO = :photo WHERE PAT_ID = :id", nativeQuery= true)
    int updateLockByCode(
    		@Param("firstName") String firstName, @Param("secondName") String secondName, @Param("name") String name,
    		@Param("bdate") Date bdate, @Param("age") Integer age, @Param("ageType") String ageType, @Param("sex") char sex, @Param("address") String address, @Param("city") String city,
    		@Param("nextKin") String nextKin, @Param("telephone") String telephone, @Param("mother") char mother, @Param("motherName") String motherName,
    		@Param("father") char father, @Param("fatherName") String fatherName, @Param("bType") String bType, @Param("esta") char esta, @Param("ptoge") char ptoge,
    		@Param("note") String note, @Param("taxCode") String taxCode, @Param("lock") Integer lock, @Param("photo") byte[] photo,
    		@Param("id") Integer id); 
    @Query(value = "SELECT * FROM PATIENT "
    		+"LEFT JOIN (SELECT PEX_PAT_ID, PEX_HEIGHT AS PAT_HEIGHT, PEX_WEIGHT AS PAT_WEIGHT FROM PATIENTEXAMINATION GROUP BY PEX_PAT_ID ORDER BY PEX_DATE DESC) "
    		+"AS HW ON PAT_ID = HW.PEX_PAT_ID WHERE (PAT_DELETED='N' or PAT_DELETED is null) "
    		+"AND (PAT_AFFILIATED_PERSON < 1 OR PAT_AFFILIATED_PERSON is null) "
    		+"AND (PAT_IS_HEAD_AFFILIATION = 1) "
    		+"ORDER BY PAT_ID DESC", 
    		nativeQuery= true)
    ArrayList<Patient> getPatientsHeadWithHeightAndWeight();
}