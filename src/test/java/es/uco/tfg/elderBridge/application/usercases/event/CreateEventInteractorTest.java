package es.uco.tfg.elderBridge.application.usercases.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import static org.junit.jupiter.api.Assertions.*;


import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.EventAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.EventEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.ParticipantRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;


@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo tests Caso de uso CreateEvent")
public class CreateEventInteractorTest {
	
	private EventAdapter eventAdapter;
	
	private CreateEventInteractor createEventInteractor;
	@Mock
	private EventEntityRepositoryJPA eventEntityRepositoryJPA;
	
	@Mock
	private ParticipantRepositoryJPA participantRepositoryJPA;
	
	@Mock
	private UserRepositoryJPA userRepositoryJPA;
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	Clock clock = Clock.fixed( Instant.parse("2026-08-10T10:15:30Z"), ZoneId.of("UTC"));
	
	private EventEntity eventEntity;
	
	private EventInput eventInput;
	
	private UserEntity userEntity; 
	
	private SocietyEntity societyEntity;
	@BeforeEach
	void setUp() {
		userEntity = UserEntity.builder()
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
			.userId(1L)
			.societyId(1L)
			.build();
		
		eventEntity = EventEntity.builder()
				.description("descripcion ejemplo")
				.eventDate(LocalDateTime.now(clock))
				.eventLocation("Calle ejemplo")
				.name("nombreEvento")
				.societyOrganizer(societyEntity)
				.userCreator(userEntity)
				.build();
		List<EventEntity> list = new ArrayList<>();
		list.add(eventEntity);
	societyEntity.setAvailablesEvent(list);
		
		
		
		eventAdapter = new EventAdapter(eventEntityRepositoryJPA, participantRepositoryJPA, userRepositoryJPA,null, societyRepositoryJPA);
		createEventInteractor =  new CreateEventInteractor(eventAdapter);
		
	}
	
	@Test
	@DisplayName("Happy Case CreateEvent")
	void shouldCreateEventSucessfully() {
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.empty());
		when(userRepositoryJPA.findById(1L)).thenReturn(Optional.of(userEntity));
		when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.of(societyEntity));
		
		EventOutput result = createEventInteractor.doInteractor(eventInput);
	
		assertEquals(eventInput.getName(),result.getName());
		assertEquals(eventInput.getDescription(), result.getDescription());
		assertEquals(eventInput.getEventDate(), result.getEventDate());
		assertEquals(eventInput.getEventLocation(), result.getEventLocation());
		
		verify(eventEntityRepositoryJPA, times(1)).findEventByName("nombreEvento");
		}
	
	@Test
	@DisplayName("Caso KO el nombre esta en uso")
	void nameIsAlredadyUsed() {
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.of(eventEntity));
		
		
		DomainException result = assertThrows(DomainException.class,()->createEventInteractor.doInteractor(eventInput));
		
		 assertEquals(result.getName(), DomainErrorList.NombreEventoEnUso.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.NombreEventoEnUso.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	     verify(eventEntityRepositoryJPA, times(1)).findEventByName("nombreEvento");
	     verify(eventEntityRepositoryJPA, never()).save(any(EventEntity.class));
		
	}
	
	
	@Test
	@DisplayName("Caso KO la fecha actual es posterior a la fecha del evento")
	void eventDateIsBeforeThatCurrentDate() {
		eventInput.setEventDate(LocalDateTime.now().minusDays(1));
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.empty());
		
		
		DomainException result = assertThrows(DomainException.class,()->createEventInteractor.doInteractor(eventInput));
		
		 assertEquals(result.getName(), DomainErrorList.FechasInvalidas.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.FechasInvalidas.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	     verify(eventEntityRepositoryJPA, times(1)).findEventByName("nombreEvento");
	     verify(eventEntityRepositoryJPA, never()).save(any(EventEntity.class));

		
	}
	
}
