package org.isf.dicom.service;

import java.util.List;

import org.isf.dicom.model.FileDicom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DicomIoOperationRepository extends JpaRepository<FileDicom, Long> {
    List<FileDicom> findAllByOrderByFileNameAsc();
    
    @Query(value = "SELECT * FROM DICOM WHERE DM_PAT_ID = :id AND DM_FILE_SER_NUMBER = :file ORDER BY DM_FILE_NOME", nativeQuery= true)
    List<FileDicom> findAllWhereIdAndNumberByOrderNameAsc(@Param("id") Long id, @Param("file") String file);
    @Query(value = "SELECT * FROM DICOM WHERE DM_PAT_ID = :id GROUP BY DM_FILE_SER_INST_UID", nativeQuery= true)
    List<FileDicom> findAllWhereIdGroupByUid(@Param("id") Long id);
    @Query(value = "SELECT * FROM DICOM WHERE DM_PAT_ID = :id AND DM_FILE_SER_NUMBER = :file AND DM_FILE_INST_UID = :uid", nativeQuery= true)
    List<FileDicom> findAllWhereIdAndFileAndUid(@Param("id") Long id, @Param("file") String file, @Param("uid") String uid);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DICOM WHERE DM_PAT_ID = :id AND DM_FILE_SER_NUMBER = :file", nativeQuery= true)
    void deleteByIdAndNumber(@Param("id") Long id, @Param("file") String file);
}