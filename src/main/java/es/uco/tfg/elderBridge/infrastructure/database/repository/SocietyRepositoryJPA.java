package es.uco.tfg.elderBridge.infrastructure.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;

public interface SocietyRepositoryJPA extends JpaRepository<SocietyEntity, Long>{

	Optional<SocietyEntity> findSocietyByName(String name);

	@Query("SELECT m FROM SOCIETY m WHERE m.creator.userId = :idUserCreator")
	Optional<SocietyEntity> findSocietyByIdUserCreator(@Param("idUserCreator") Long idUserCreator);
	
	Optional<SocietyEntity> findSocietyByEmail(String email);

}
