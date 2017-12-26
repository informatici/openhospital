package org.isf.menu.service;

import java.util.List;

import org.isf.menu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserIoOperationRepository extends JpaRepository<User, String> {
    public List<User> findAllByOrderByUserNameAsc();    

    @Query(value = "SELECT * FROM USER WHERE US_UG_ID_A = :groupId ORDER BY US_ID_A", nativeQuery= true)
    public List<User> findAllWhereUserGroupNameByOrderUserNameAsc(@Param("groupId") String groupId);
    
    @Modifying
    @Query(value = "UPDATE USER SET US_DESC = :description WHERE US_ID_A = :id", nativeQuery= true)
    int updateDescription(@Param("description") String description, @Param("id") String id); 	

    @Modifying
    @Query(value = "UPDATE USER SET US_PASSWD = :password WHERE US_ID_A = :id", nativeQuery= true)
    int updatePassword(@Param("password") String password, @Param("id") String id); 	
}