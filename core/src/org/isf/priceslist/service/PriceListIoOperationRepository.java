package org.isf.priceslist.service;

import org.isf.priceslist.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PriceListIoOperationRepository extends JpaRepository<PriceList, Integer> {
    
	@Modifying
    @Transactional
    @Query(value = "INSERT INTO PRICELISTS (LST_CODE, LST_NAME, LST_DESC, LST_CURRENCY) VALUES (:code,:name,:description,:currency)", nativeQuery= true)
    void insertPriceList(
            @Param("code") String code, @Param("name") String name,
            @Param("description") String description, @Param("currency") String currency);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE PRICELISTS SET LST_CODE = :code, LST_NAME = :name, LST_DESC = :description, LST_CURRENCY = :currency WHERE LST_ID = :id", nativeQuery= true)
    int updatePriceList(
            @Param("code") String code, @Param("name") String name,
            @Param("description") String description, @Param("currency") String currency, @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PRICELISTS WHERE LST_ID = :id", nativeQuery= true)
    void deleteWhereId(@Param("id") Integer id);
}