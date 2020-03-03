package org.isf.menu.service;

import org.isf.menu.model.GroupMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GroupMenuIoOperationRepository extends JpaRepository<GroupMenu, Integer> {

	@Modifying
    @Query(value = "INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) values(:groupId, :menuId, :active)", nativeQuery= true)
    int insert(@Param("groupId") String groupId, @Param("menuId") String menuId, @Param("active") String acive);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM GROUPMENU WHERE GM_UG_ID_A = :id", nativeQuery= true)
    void deleteWhereUserGroup(@Param("id") String id);
       
}