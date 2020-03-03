package org.isf.menu.service;

import org.isf.menu.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserGroupIoOperationRepository extends JpaRepository<UserGroup, String> {
    List<UserGroup> findAllByOrderByCodeAsc();

    @Modifying
    @Transactional
    @Query(value =  "UPDATE USERGROUP SET UG_DESC = :description WHERE UG_ID_A = :id", nativeQuery= true)
    int updateDescription(@Param("description") String description, @Param("id") String id);    
}