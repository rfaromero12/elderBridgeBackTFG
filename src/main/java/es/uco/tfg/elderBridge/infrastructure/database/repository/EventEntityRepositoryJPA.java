package es.uco.tfg.elderBridge.infrastructure.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;

public interface EventEntityRepositoryJPA extends JpaRepository<EventEntity, Long>{

	Optional<EventEntity> findEventByName(String name);

	int deleteByName(String name);

	@Query("SELECT m FROM EVENT m WHERE m.societyOrganizer.name = :name")
	List<EventEntity> findEventsByName(String name);

}
