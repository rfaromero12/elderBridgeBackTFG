package es.uco.tfg.elderBridge.application.usercases.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.EventAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.ParticipantEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.EventEntityRepositoryJPA;

@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo tests Caso de uso DeleteEvent")
public class DeleteEventInteractorTest {
private EventAdapter eventAdapter;
	
	private DeleteEventInteractor deleteEventInteractor;
	@Mock
	private EventEntityRepositoryJPA eventEntityRepositoryJPA;
	
	Clock clock = Clock.fixed( Instant.parse("2025-08-08T10:15:30Z"), ZoneId.of("UTC"));
	
	private EventEntity eventEntity;
	
	private EventInput eventInput;
	
	private UserEntity userEntity;
	private SocietyEntity societyEntity;
	
	@BeforeEach
	void setUp() {
		userEntity =  UserEntity.builder()
                .userId(1L)
                .email("nuevo@example.com")
                .password("hashedPassword123")
                .name("Nuevo")
                .surname("Usuario")
                .build();
		societyEntity =	SocietyEntity.builder()
				.creator(null)
				.email("ejemplo@gmail.com")
				.societyId(1L)
				.location("locationEjemplo")
				.name("nombreEjemplo")
				.memberships(null)
				.description("description").build();	
		eventInput = EventInput.builder()
			.description("descripcion ejemplo")
			.eventDate(LocalDateTime.now(clock))
			.eventLocation("Calle ejemplo")
			.name("nombreEvento")
			.build();
		eventEntity = EventEntity.builder()
				.description("descripcion ejemplo")
				.eventDate(LocalDateTime.now(clock))
				.eventLocation("Calle ejemplo")
				.name("nombreEvento")
				.societyOrganizer(societyEntity)
				.userCreator(userEntity)
				.build();
		List<ParticipantEntity> list = new ArrayList<>();
		List<EventEntity> listEvent = new ArrayList<>();
		ParticipantEntity.builder().participantId(1L).event(eventEntity).member(userEntity);
		eventEntity.setParticipantEntities(list);
		listEvent.add(eventEntity);
		userEntity.setEventCreatedBy(listEvent);
		societyEntity.setAvailablesEvent(listEvent);

		eventAdapter = new EventAdapter(eventEntityRepositoryJPA, null, null,null,null);
		deleteEventInteractor =  new DeleteEventInteractor(eventAdapter);
		
	}
	
	@Test
	@DisplayName("HappyCase")
	void shouldBeDeleted() {
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.of(eventEntity));
    	
    	deleteEventInteractor.doInteractor(eventInput);

    	verify(eventEntityRepositoryJPA,times(1)).findEventByName("nombreEvento");
    	verify(eventEntityRepositoryJPA,times(1)).delete(eventEntity);

	}
	
	@Test
	@DisplayName("Caso KO")
	void shouldNotBeDeleted() {
		
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.empty());
    	
		
		DomainException exception =assertThrows(DomainException.class, 
				()->deleteEventInteractor.doInteractor(eventInput));
		assertEquals(exception.getName(), DomainErrorList.EventoNoEncontrado.getName());
		 assertEquals(exception.getDescripcion(), DomainErrorList.EventoNoEncontrado.getDescription());
		 assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	    	verify(eventEntityRepositoryJPA,times(1)).findEventByName("nombreEvento");

	    	verify(eventEntityRepositoryJPA,times(0)).delete(eventEntity);
    	
	}
	
}
