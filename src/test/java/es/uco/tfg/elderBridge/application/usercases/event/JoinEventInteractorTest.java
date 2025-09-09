package es.uco.tfg.elderBridge.application.usercases.event;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;


import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;

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

import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.EventAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.ParticipantEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.EventEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.ParticipantRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;

@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo caso de uso Join Event")
public class JoinEventInteractorTest {
		
	private JoinEventInteractor joinEventInteractor;
	
	@Mock
	private ParticipantRepositoryJPA participantRepositoryJPA;
	@Mock
	private UserRepositoryJPA userRepositoryJPA;
	@Mock
	private EventEntityRepositoryJPA eventEntityRepositoryJPA;

	
	Clock clock = Clock.fixed( Instant.parse("2025-08-10T10:15:30Z"), ZoneId.of("UTC"));
	
	private EventEntity eventEntity;
	
	private EventAdapter eventAdapter;
	
	private UserAdapter userAdapter;
	private UserEntity userEntity;
	private SocietyEntity societyEntity;
	private UserEntity userEntity2;
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
		eventEntity = EventEntity.builder()
				.description("descripcion ejemplo")
				.eventDate(LocalDateTime.now(clock))
				.eventLocation("Calle ejemplo")
				.name("nombreEvento")
				.participantEntities(null)
				.userCreator(userEntity)
				.societyOrganizer(societyEntity)
				.build();
		
		List<ParticipantEntity> list = new ArrayList<>();
		userEntity2 = UserEntity.builder()
                .userId(2L)
                .email("nuevo@dddexample.com")
                .password("hashedPassword123")
                .name("Nuevoaaa")
                .surname("Usuario")
                .build();
		ParticipantEntity.builder().participantId(1L).event(eventEntity).member(userEntity2);
		
		eventEntity.setParticipantEntities(list);
		
		userAdapter = new UserAdapter(userRepositoryJPA, null);
		eventAdapter = new EventAdapter(eventEntityRepositoryJPA, participantRepositoryJPA, userRepositoryJPA, null, null);
		joinEventInteractor = new JoinEventInteractor(eventAdapter, userAdapter);
	}
	
	@Test
	@DisplayName("Happy Case")
	void shouldBeJoined() {
		when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.of(eventEntity));
	
		joinEventInteractor.doInteractor("nuevo@example.com", "nombreEvento");
	
		verify(userRepositoryJPA,  times(2)).findUserByEmail("nuevo@example.com");
		verify(eventEntityRepositoryJPA,  times(2)).findEventByName("nombreEvento");

	}
	
	@Test
	@DisplayName("User not found")
	void userNotFound() {
		when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.empty());
	
		DomainException result = assertThrows(DomainException.class,()->joinEventInteractor.doInteractor("nuevo@example.com", "nombreEvento"));
	
		verify(userRepositoryJPA,  times(1)).findUserByEmail("nuevo@example.com");
		verify(eventEntityRepositoryJPA,  times(0)).findEventByName("nombreEvento");
		verify(participantRepositoryJPA,  times(0)).save(any(ParticipantEntity.class));
		 assertEquals(result.getName(), DomainErrorList.UsuarioNoEncontrado.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.UsuarioNoEncontrado.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	}
	
	

	@Test
	@DisplayName("event not found")
	void eventNotFound() {
		when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
		when(eventEntityRepositoryJPA.findEventByName("nombreEvento")).thenReturn(Optional.empty());
	
		DomainException result = assertThrows(DomainException.class,()->joinEventInteractor.doInteractor("nuevo@example.com", "nombreEvento"));
	
		verify(userRepositoryJPA,  times(1)).findUserByEmail("nuevo@example.com");
		verify(eventEntityRepositoryJPA,  times(1)).findEventByName("nombreEvento");
		verify(participantRepositoryJPA,  times(0)).save(any(ParticipantEntity.class));
		 assertEquals(result.getName(), DomainErrorList.EventoNoEncontrado.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.EventoNoEncontrado.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	}
}
