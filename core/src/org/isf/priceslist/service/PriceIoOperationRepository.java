package org.isf.priceslist.service;

import java.util.List;

import org.isf.priceslist.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PriceIoOperationRepository extends JpaRepository<Price, Integer> {
    List<Price> findAllByOrderByDescriptionAsc();
	
    @Query(value = "SELECT * FROM PRICES WHERE PRC_LST_ID = :id", nativeQuery= true)
    List<Price> findAllWhereList(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PRICES WHERE PRC_LST_ID = :listId", nativeQuery= true)
    void deleteWhereList(@Param("listId") Integer listId);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PRICES (PRC_LST_ID, PRC_GRP, PRC_ITEM, PRC_DESC, PRC_PRICE) VALUES (:listId,:group,:item,:description,:price)", nativeQuery= true)
    void insertPrice(
            @Param("listId") Integer listId, @Param("group") String group, @Param("item") String item,
            @Param("description") String description, @Param("price") Double price);
    
}