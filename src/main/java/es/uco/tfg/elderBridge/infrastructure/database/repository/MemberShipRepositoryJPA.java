package es.uco.tfg.elderBridge.infrastructure.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uco.tfg.elderBridge.infrastructure.database.entity.MembershipEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import jakarta.transaction.Transactional;

public interface MemberShipRepositoryJPA extends JpaRepository<MembershipEntity, Long>{
	@Query("SELECT m.society FROM MEMBERSHIP m WHERE m.member.email = :email")
	List<SocietyEntity> findSocietyByUserEmail(@Param("email") String email);
	
	
	@Modifying
	@Transactional
	@Query("DELETE FROM MEMBERSHIP m WHERE m.member.email = :email AND m.society.name = :name")
	int unJoin(@Param("email") String email, @Param("name") String name);

	
	
}
