package org.isf.sms.service;

import java.util.Date;
import java.util.List;

import org.isf.sms.model.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SmsIoOperationRepository extends JpaRepository<Sms, Integer> {

    @Query(value = "SELECT * FROM SMS WHERE DATE(SMS_DATE_SCHED) BETWEEN :start AND :stop ORDER BY SMS_DATE_SCHED ASC", nativeQuery= true)
    List<Sms> findAllWhereBetweenDatesByOrderDate(@Param("start") Date start, @Param("stop") Date stop);
        
    @Query(value = "SELECT * FROM SMS WHERE DATE(SMS_DATE_SCHED) BETWEEN :start AND :stop AND SMS_DATE_SENT IS NULL ORDER BY SMS_DATE_SCHED ASC", nativeQuery= true)
    List<Sms> findAllWhereSentNotNullBetweenDatesByOrderDate(@Param("start") Date start, @Param("stop") Date stop);
	
    @Query(value = "SELECT * FROM SMS WHERE SMS_DATE_SENT IS NULL ORDER BY SMS_DATE_SCHED ASC", nativeQuery= true)
    List<Sms> findAllWhereSentNotNullByOrderDate();
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SMS WHERE SMS_MOD = :mod AND SMS_MOD_ID = :id AND SMS_DATE_SENT IS NULL", nativeQuery= true)
    void deleteWhereModuleAndId(@Param("mod") String mod, @Param("id") String id);
}