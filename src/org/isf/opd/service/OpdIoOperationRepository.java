package org.isf.opd.service;

import java.util.List;

import org.isf.opd.model.Opd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OpdIoOperationRepository extends JpaRepository<Opd, Integer>, OpdIoOperationRepositoryCustom {    
	
	@Query(value = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID ORDER BY OPD_PROG_YEAR DESC", nativeQuery= true)
    List<Opd> findAllByOrderByProgYearDesc();
	
	@Query(value = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID WHERE OPD_PAT_ID = :code ORDER BY OPD_PROG_YEAR DESC", nativeQuery= true)
    List<Opd> findAllWherePatIdByOrderByProgYearDesc(@Param("code") Integer code);
	
	@Query(value = "SELECT MAX(OPD_PROG_YEAR) FROM OPD", nativeQuery= true)
    Integer findMaxProgYear();

	@Query(value = "SELECT MAX(OPD_PROG_YEAR) FROM OPD WHERE YEAR(OPD_DATE) = :date", nativeQuery= true)
    Integer findMaxProgYearWhereDate(@Param("date") Integer date);

	@Query(value = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID WHERE OPD_PAT_ID = ? ORDER BY OPD_DATE DESC LIMIT 1", nativeQuery= true)
    List<Opd> findAllWherePatIdByOrderByDateDescLimit1(@Param("code") Integer code);
	
	@Query(value = "SELECT * FROM OPD WHERE OPD_PROG_YEAR = :prog_year", nativeQuery= true)
    List<Opd> findAllByProgYear(@Param("prog_year") Integer prog_year);
	
	@Query(value = "SELECT * FROM OPD WHERE OPD_PROG_YEAR = :prog_year AND YEAR(OPD_DATE_VIS) = :year", nativeQuery= true)
    List<Opd> findAllByProgYearWithinYear(@Param("prog_year") Integer prog_year, @Param("year") Integer year);
}
