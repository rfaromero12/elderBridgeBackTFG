package es.uco.tfg.elderBridge.infrastructure.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.ParticipantEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;

public interface ParticipantRepositoryJPA extends JpaRepository<ParticipantEntity, Long>{
	@Query("SELECT m.event FROM PARTICIPANT m WHERE m.member.email = :email")
	List<EventEntity> findEventsByUserEmail(@Param("email") String email);

	@Query("SELECT m.member FROM PARTICIPANT m WHERE m.event.eventId = :eventId")
	List<UserEntity> findUserByEventId(@Param("eventId")Long eventId);

}
