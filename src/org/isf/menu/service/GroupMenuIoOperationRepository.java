package org.isf.menu.service;

import org.isf.menu.model.GroupMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface GroupMenuIoOperationRepository extends JpaRepository<GroupMenu, Integer> {

    @Query(value = "INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) values(:groupId, :menuId, :active)", nativeQuery= true)
    public int insert(@Param("groupId") String groupId, @Param("menuId") String menuId, @Param("active") String acive);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM GROUPMENU WHERE GM_UG_ID_A = :id", nativeQuery= true)
    public void deleteWhereUserGroup(@Param("id") String id); 	
       
}