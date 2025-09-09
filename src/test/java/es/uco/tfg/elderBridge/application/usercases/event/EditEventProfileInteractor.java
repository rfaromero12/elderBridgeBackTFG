package es.uco.tfg.elderBridge.application.usercases.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;

import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.EventAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.EventEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.ParticipantRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;



@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo tests caso uso EditEvent")
public class EditEventProfileInteractor {
	private EditEventInteractor editEventInteractor;
	@Mock
	private EventEntityRepositoryJPA eventEntityRepositoryJPA;
	
	Clock clock = Clock.fixed( Instant.parse("2026-08-08T10:15:30Z"), ZoneId.of("UTC"));
	
	private EventEntity eventEntity;
	
	private EventInput eventInput;
	
	private EventAdapter eventAdapter;
	
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	@Mock
	private MailService mailService;
	@Mock
	private ParticipantRepositoryJPA participantRepositoryJPA;
	
	private SocietyEntity societyEntity;
	
	private UserEntity userEntity;
	
	private List<UserEntity> participantsList; 
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
			.eventId(1L)
			.build();
		eventEntity = EventEntity.builder()
				.description("descripcion ejemplo")
				.eventDate(LocalDateTime.now(clock))
				.eventLocation("Calle ejemplo")
				.name("nombreEvento")
				.eventId(1L)
				.userCreator(userEntity)
				.societyOrganizer(societyEntity)
				.build();
		List<EventEntity> list = new ArrayList<>();
		list.add(eventEntity);
		societyEntity.setAvailablesEvent(list);
		
				
		
		participantsList = List.of(
        UserEntity.builder()
                .userId(1L)
                .email("nuevo@example.com")
                .password("hashedPassword123")
                .name("Nuevo")
                .surname("Usuario")
                .build());
		eventAdapter = new EventAdapter(eventEntityRepositoryJPA, participantRepositoryJPA, null, mailService,societyRepositoryJPA);
		editEventInteractor =  new EditEventInteractor(eventAdapter);
		
	}
	
	@Test
	@DisplayName("Happy Case")
	void shouldBeEdited() {
		when(eventEntityRepositoryJPA.findById(1L)).thenReturn(Optional.of(eventEntity));
		
		
		EventOutput result=  editEventInteractor.doInteractor(eventInput);
		
		verify(eventEntityRepositoryJPA, times(2)).findById(1L);
		
	}
	
	@Test
	@DisplayName("Caso KO el nombre esta en uso")
	void nameIsAlreadyUsed() {
		eventEntity.setName("NombreAojo");
		when(eventEntityRepositoryJPA.findById(1L)).thenReturn(Optional.of(eventEntity));
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.of(eventEntity));

		
		DomainException result = assertThrows(DomainException.class,()->editEventInteractor.doInteractor(eventInput));
		
		 assertEquals(result.getName(), DomainErrorList.NombreEventoEnUso.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.NombreEventoEnUso.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	     verify(eventEntityRepositoryJPA, times(1)).findById(1L);
	     verify(eventEntityRepositoryJPA, never()).save(any(EventEntity.class));
		
	}
	
	
	@Test
	@DisplayName("Caso KO la fecha actual es posterior a la fecha del evento")
	void eventDateIsBeforeThatCurrentDate() {
		eventInput.setEventDate(LocalDateTime.now().minusDays(1));
		when(eventEntityRepositoryJPA.findById(1L)).thenReturn(Optional.of(eventEntity));
		
		
		DomainException result = assertThrows(DomainException.class,()->editEventInteractor.doInteractor(eventInput));
		
		 assertEquals(result.getName(), DomainErrorList.FechasInvalidas.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.FechasInvalidas.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	     verify(eventEntityRepositoryJPA, times(1)).findById(1L);
	     verify(eventEntityRepositoryJPA, never()).save(any(EventEntity.class));

		
	}
	
	@Test
	@DisplayName("Caso KO el nombre esta en uso")
	void eventNotFound() {
		when(eventEntityRepositoryJPA.findById(1L)).thenReturn(Optional.empty());
		
		
		DomainException result = assertThrows(DomainException.class,()->editEventInteractor.doInteractor(eventInput));
		
		 assertEquals(result.getName(), DomainErrorList.EventoNoEncontrado.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.EventoNoEncontrado.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	     verify(eventEntityRepositoryJPA, times(1)).findById(1L);
	     verify(eventEntityRepositoryJPA, never()).save(any(EventEntity.class));
		
	}
	    
}
