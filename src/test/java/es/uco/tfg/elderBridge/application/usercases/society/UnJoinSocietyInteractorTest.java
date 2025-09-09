package es.uco.tfg.elderBridge.application.usercases.society;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.SocietyAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.MembershipEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.MemberShipRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.CreateSocietyDTO;

@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo caso de uso UnJoin Event")
public class UnJoinSocietyInteractorTest {
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	@Mock
	private UserRepositoryJPA userRepositoryJPA;
	
	@Mock
	private MemberShipRepositoryJPA memberShipRepositoryJPA;
	
	private UnjoinSocietyInteractor unjoinSocietyInteractor;
	
	private SocietyInput societyInput;
	
	private SocietyEntity societyEntity;
	
	private SocietyAdapter societyAdapter;
	
	private UserEntity userEntity;

	private UserAdapter userAdapter;

	
	
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
		.societyId(null)
		.location("locationEjemplo")
		.name("nombreEjemplo")
		.memberships(null)
		.description("description").build();	
	societyAdapter = new SocietyAdapter(societyRepositoryJPA, userRepositoryJPA, memberShipRepositoryJPA);
	societyInput = SocietyInput.builder()
			.idUserCreator(null)
			.email("ejemplo@gmail.com")
			.idSociety(null)
			.location("locationEjemplo")
			.name("nombreEjemplo")
			.description("description").build();
	
	
	userAdapter = new UserAdapter(userRepositoryJPA, memberShipRepositoryJPA);
	unjoinSocietyInteractor = new UnjoinSocietyInteractor(societyAdapter, userAdapter);
	}
	
	@Test
	@DisplayName("Happy Case")
	void shouldBeUnjoinedSuccesfully() {
		when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.of(societyEntity));
	
		unjoinSocietyInteractor.doInteractor("nuevo@example.com", "nombreEjemplo");
	
		verify(userRepositoryJPA,  times(1)).findUserByEmail("nuevo@example.com");
		verify(societyRepositoryJPA,  times(1)).findSocietyByName("nombreEjemplo");
		verify(memberShipRepositoryJPA,  times(1)).unJoin("nuevo@example.com", "nombreEjemplo");

	}
	
	@Test
	@DisplayName("User not found")
	void userNotFound() {
		when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.empty());
	
		DomainException result = assertThrows(DomainException.class,()->unjoinSocietyInteractor.doInteractor("nuevo@example.com", "nombreEjemplo"));
	
		verify(userRepositoryJPA,  times(1)).findUserByEmail("nuevo@example.com");
		verify(societyRepositoryJPA,  times(0)).findSocietyByName("nombreEjemplo");
		verify(memberShipRepositoryJPA,  times(0)).delete(any(MembershipEntity.class));
		 assertEquals(result.getName(), DomainErrorList.UsuarioNoEncontrado.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.UsuarioNoEncontrado.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	}
	
	

	@Test
	@DisplayName("event not found")
	void eventNotFound() {
		when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.empty());
	
		DomainException result = assertThrows(DomainException.class,()->unjoinSocietyInteractor.doInteractor("nuevo@example.com", "nombreEjemplo"));
	
		verify(userRepositoryJPA,  times(1)).findUserByEmail("nuevo@example.com");
		verify(societyRepositoryJPA,  times(1)).findSocietyByName("nombreEjemplo");
		verify(memberShipRepositoryJPA,  times(0)).delete(any(MembershipEntity.class));
		 assertEquals(result.getName(), DomainErrorList.ONGNoEncontrada.getName());
		 assertEquals(result.getDescripcion(), DomainErrorList.ONGNoEncontrada.getDescription());
		 assertEquals(result.getStatus(), HttpStatus.BAD_REQUEST);
	}
}
