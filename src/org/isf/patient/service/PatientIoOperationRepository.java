package org.isf.patient.service;

import java.util.List;

import org.isf.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PatientIoOperationRepository extends JpaRepository<Patient, Integer>, PatientIoOperationRepositoryCustom {
    public List<Patient> findAllByOrderByDescriptionAsc();    

    @Query(value = "SELECT * FROM PATIENT WHERE (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_NAME", nativeQuery= true)
    public List<Patient> findAllWhereDeleted();    

    @Query(value = "SELECT * FROM PATIENT WHERE PAT_NAME = :name AND (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_SNAME,PAT_FNAME", nativeQuery= true)
    public List<Patient> findAllWhereNameAndDeletedOrderedByName(@Param("name") String name);

    @Query(value = "SELECT * FROM PATIENT WHERE PAT_ID = :id AND (PAT_DELETED='N' OR PAT_DELETED IS NULL)", nativeQuery= true)
    public List<Patient> findAllWhereIdAndDeleted(@Param("id") Integer id);
    
    @Query(value = "SELECT * FROM PATIENT WHERE PAT_ID = ?", nativeQuery= true)
    public List<Patient> findAllWhereId(@Param("id") Integer id);
    
    @Modifying
    @Query("UPDATE PATIENT SET PAT_DELETED = 'Y' WHERE PAT_ID = :id")
    int updateDeleted(@Param("id") Integer id);
    
    @Query(value = "SELECT * FROM PATIENT WHERE PAT_NAME = :name AND PAT_DELETED='N'", nativeQuery= true)
    public List<Patient> findAllWhereName(@Param("name") String name);

    @Query(value = "SELECT MAX(PAT_ID) FROM PATIENT", nativeQuery= true)
    public Integer findMaxCode();
        
    @Modifying
    @Query("UPDATE ADMISSION SET ADM_PAT_ID = :new_id WHERE ADM_PAT_ID = :old_id")
    int updateAdmission(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);

    @Modifying
    @Query("UPDATE PATIENTEXAMINATION SET PEX_PAT_ID = :new_id WHERE PEX_PAT_ID = :old_id")
    int updateExamination(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);
    
    @Modifying
    @Query("UPDATE LABORATORY SET LAB_PAT_ID = :new_id, LAB_PAT_NAME = :name, LAB_AGE = age, LAB_SEX = :sex WHERE LAB_PAT_ID = :old_id")
    int updateLaboratory(@Param("new_id") Integer new_id, @Param("name") String name, @Param("age") Integer age, @Param("sex") String sex, @Param("old_id") Integer old_id);

    @Modifying
    @Query("UPDATE OPD SET OPD_PAT_ID = :new_id, OPD_AGE = :age, OPD_SEX = :sex WHERE OPD_PAT_ID = :old_id")
    int updateOpd(@Param("new_id") Integer new_id, @Param("age") Integer age, @Param("sex") String sex, @Param("old_id") Integer old_id);

    @Modifying
    @Query("UPDATE BILLS SET BLL_ID_PAT = :new_id, BLL_PAT_NAME = :name WHERE BLL_ID_PAT = :old_id")
    int updateBill(@Param("new_id") Integer new_id, @Param("name") String name, @Param("old_id") Integer old_id);

    @Modifying
    @Query("UPDATE MEDICALDSRSTOCKMOVWARD SET MMVN_PAT_ID = :new_id WHERE MMVN_PAT_ID = :old_id")
    int updateMedicalStock(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);


    @Modifying
    @Query("UPDATE THERAPIES SET THR_PAT_ID = :new_id WHERE THR_PAT_ID = :old_id")
    int updateTherapy(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);

    @Modifying
    @Query("UPDATE VISITS SET VST_PAT_ID = :new_id WHERE VST_PAT_ID = :old_id")
    int updateVisit(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);

    @Modifying
    @Query("UPDATE PATIENTVACCINE SET PAV_PAT_ID = :new_id WHERE PAV_PAT_ID = :old_id")
    int updatePatientVaccine(@Param("new_id") Integer new_id, @Param("old_id") Integer old_id);
 		

    @Modifying
    @Query("UPDATE PATIENT SET PAT_DELETED = 'Y' WHERE PAT_ID = :id")
    int updateDelete(@Param("id") Integer id); 		
}