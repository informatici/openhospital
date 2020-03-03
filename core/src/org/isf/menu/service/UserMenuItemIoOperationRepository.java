package org.isf.menu.service;

import org.isf.menu.model.UserMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMenuItemIoOperationRepository extends JpaRepository<UserMenuItem, String> {

    @Query(value = "select mn.*,GROUPMENU.GM_ACTIVE as IS_ACTIVE from USERGROUP inner join USER on US_UG_ID_A=UG_ID_A "
			+ " inner join GROUPMENU on UG_ID_A=GM_UG_ID_A inner join MENUITEM as mn on "
			+ " GM_MNI_ID_A=mn.MNI_ID_A where US_ID_A = :id order by MNI_POSITION", nativeQuery= true)
    List<Object[]> findAllWhereId(@Param("id") String id);

    @Query(value = "select mn.*,GROUPMENU.GM_ACTIVE from USERGROUP "
			+ " inner join GROUPMENU on UG_ID_A=GM_UG_ID_A inner join MENUITEM as mn on "
			+ " GM_MNI_ID_A=mn.MNI_ID_A where UG_ID_A = :groupId order by MNI_POSITION", nativeQuery= true)
    List<UserMenuItem> findAllWhereGroupId(@Param("groupId") String groupId);
    
}