package es.uco.tfg.elderBridge.application.usercases.event;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.EventAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.ParticipantRepositoryJPA;

@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo Caso de Uso ShowSubsEvent")
public class ShowSubsEventInteractorTests {
	
	private ShowSubsEventInteractor showSubsEventInteractor;
	@Mock
	private ParticipantRepositoryJPA participantRepositoryJPA;
	
	Clock clock = Clock.fixed( Instant.parse("2025-08-10T10:15:30Z"), ZoneId.of("UTC"));
	
	private List<EventEntity> eventEntity;
	private UserEntity userEntity;
	private SocietyEntity societyEntity;
	
	private EventAdapter eventAdapter;
	
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
				.creator(userEntity)
				.email("ejemplo@gmail.com")
				.societyId(1L)
				.location("locationEjemplo")
				.name("nombreEjemplo")
				.memberships(null)
				.description("description").build();
		eventEntity =  List.of(EventEntity.builder()
							.description("descripcion ejemplo")
							.eventDate(LocalDateTime.now(clock))
							.eventLocation("Calle ejemplo")
							.name("nombreEvento")
							.societyOrganizer(societyEntity)
							.userCreator(userEntity)
							.build(),
						EventEntity.builder()
							.description("descripcion ejemplo")
							.eventDate(LocalDateTime.now(clock))
							.eventLocation("Calle ejemplo")
							.societyOrganizer(societyEntity)
							.userCreator(userEntity)
							.name("nombreEvento2")
							.build());
		
		eventAdapter = new EventAdapter(null, participantRepositoryJPA, null,null, null);
		showSubsEventInteractor = new ShowSubsEventInteractor(eventAdapter);
	}
	
	@Test
	@DisplayName("HappyCase")
	void shouldShowSubsEvent() {
		when(participantRepositoryJPA.findEventsByUserEmail("aaaaaa@aaaa.es")).thenReturn(eventEntity);
		
		List<EventOutput> result = showSubsEventInteractor.doInteractor("aaaaaa@aaaa.es");
		
	    verify(participantRepositoryJPA, times(1)).findEventsByUserEmail("aaaaaa@aaaa.es");
	    assertEquals(2, result.size());
	}
	
	@Test
	@DisplayName("KO")
	void userHaventgotEvents() {
		when(participantRepositoryJPA.findEventsByUserEmail("aaaaaa@aaaa.es")).thenReturn(null);
		
		List<EventOutput> result = showSubsEventInteractor.doInteractor("aaaaaa@aaaa.es");
		
	    verify(participantRepositoryJPA, times(1)).findEventsByUserEmail("aaaaaa@aaaa.es");
	    assertEquals(0, result.size());
	}
	
}
