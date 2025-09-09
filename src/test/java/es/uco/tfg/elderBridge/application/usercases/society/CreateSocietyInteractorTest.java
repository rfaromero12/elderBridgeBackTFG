package es.uco.tfg.elderBridge.application.usercases.society;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.SocietyAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo caso de uso Create Society")
public class CreateSocietyInteractorTest {
	
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	@Mock
	private UserRepositoryJPA userRepositoryJPA;
	
	private CreateSocietyInteractor createSocietyInteractor;
	
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
		.societyId(1L)
		.location("locationEjemplo")
		.name("nombreEjemplo")
		.memberships(null)
		.description("description").build();	
	societyAdapter = new SocietyAdapter(societyRepositoryJPA, userRepositoryJPA, null);
	societyInput = SocietyInput.builder()
			.idUserCreator(1L)
			.email("ejemplo@gmail.com")
			.idSociety(1L)
			.location("locationEjemplo")
			.name("nombreEjemplo")
			.description("description").build();
	
	
	userAdapter = new UserAdapter(userRepositoryJPA, null);
	createSocietyInteractor = new CreateSocietyInteractor(societyAdapter, userAdapter);
	}
	
	@Test
	@DisplayName("Happy Case")
	void shouldBeCreatedSuccesfully() {
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.empty());
		when(userRepositoryJPA.findById(1L)).thenReturn(Optional.of(userEntity));
		when(societyRepositoryJPA.findSocietyByEmail("ejemplo@gmail.com")).thenReturn(Optional.empty());
		when(societyRepositoryJPA.findSocietyByIdUserCreator(1L)).thenReturn(Optional.empty());

		SocietyOutput result = createSocietyInteractor.doInteractor(societyInput);
	
		assertNotNull(result);
        assertEquals("ejemplo@gmail.com", result.getEmail());
        assertEquals("nombreEjemplo", result.getName());
        assertEquals("locationEjemplo", result.getLocation());
        assertEquals("description", result.getDescription());
        assertEquals(1L, result.getIdUserCreator());
        assertNotNull(result.getIdSociety());
   
        verify(userRepositoryJPA, times(2)).findById(any(Long.class));
        verify(societyRepositoryJPA, times(1)).findSocietyByName("nombreEjemplo");
        verify(societyRepositoryJPA, times(1)).findSocietyByEmail("ejemplo@gmail.com");
        verify(societyRepositoryJPA, times(1)).findSocietyByIdUserCreator(1L);

	}
	
	
	@Test
	@DisplayName("KO name is used")
	void nameIsUsed() {
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.of(societyEntity));
	
		DomainException exception = assertThrows(DomainException.class, 
		            () -> createSocietyInteractor.doInteractor(societyInput));
		        
		assertEquals(exception.getName(), DomainErrorList.NombreOngEnUso.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.NombreOngEnUso.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		verify(userRepositoryJPA, times(0)).findById(any(Long.class));
	    verify(societyRepositoryJPA, times(1)).findSocietyByName("nombreEjemplo");
        verify(societyRepositoryJPA, times(0)).save(any(SocietyEntity.class));
        verify(societyRepositoryJPA, times(0)).findSocietyByEmail("ejemplo@gmail.com");
        verify(societyRepositoryJPA, times(0)).findSocietyByIdUserCreator(1L);

	}
	
	@Test
	@DisplayName("KO user not found")
	void userNotFound() {
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.empty());
		when(userRepositoryJPA.findById(1L)).thenReturn(Optional.empty());
		when(societyRepositoryJPA.findSocietyByIdUserCreator(1L)).thenReturn(Optional.empty());

	
		DomainException exception = assertThrows(DomainException.class, 
		            () -> createSocietyInteractor.doInteractor(societyInput));
		        
		assertEquals(exception.getName(), DomainErrorList.UsuarioNoEncontrado.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.UsuarioNoEncontrado.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		verify(userRepositoryJPA, times(1)).findById(any(Long.class));
	    verify(societyRepositoryJPA, times(1)).findSocietyByName("nombreEjemplo");
        verify(societyRepositoryJPA, times(0)).save(any(SocietyEntity.class));
        verify(societyRepositoryJPA, times(1)).findSocietyByEmail("ejemplo@gmail.com");
        verify(societyRepositoryJPA, times(1)).findSocietyByIdUserCreator(1L);

	}
	
	@Test
	void userIsAlreadyASocietyCreator() {
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.empty());
		when(societyRepositoryJPA.findSocietyByIdUserCreator(1L)).thenReturn(Optional.of(societyEntity));
        when(societyRepositoryJPA.findSocietyByEmail("ejemplo@gmail.com")).thenReturn(Optional.empty());

	
		DomainException exception = assertThrows(DomainException.class, 
		            () -> createSocietyInteractor.doInteractor(societyInput));
		        
		assertEquals(exception.getName(), DomainErrorList.LimiteOngsPorUsuarioSuperado.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.LimiteOngsPorUsuarioSuperado.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		verify(userRepositoryJPA, times(0)).findById(any(Long.class));
	    verify(societyRepositoryJPA, times(1)).findSocietyByName("nombreEjemplo");
        verify(societyRepositoryJPA, times(0)).save(any(SocietyEntity.class));
        verify(societyRepositoryJPA, times(1)).findSocietyByEmail("ejemplo@gmail.com");
        verify(societyRepositoryJPA, times(1)).findSocietyByIdUserCreator(1L);

	}
	
	@Test
	@DisplayName("KO ong email is already used")
	void ongEmailIsAlreadyUsed() {
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.empty());
        when(societyRepositoryJPA.findSocietyByEmail("ejemplo@gmail.com")).thenReturn(Optional.of(societyEntity));

	
		DomainException exception = assertThrows(DomainException.class, 
		            () -> createSocietyInteractor.doInteractor(societyInput));
		        
		assertEquals(exception.getName(), DomainErrorList.EmailEnUso.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.EmailEnUso.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		verify(userRepositoryJPA, times(0)).findById(any(Long.class));
	    verify(societyRepositoryJPA, times(1)).findSocietyByName("nombreEjemplo");
        verify(societyRepositoryJPA, times(0)).save(any(SocietyEntity.class));
        verify(societyRepositoryJPA, times(1)).findSocietyByEmail("ejemplo@gmail.com");
        verify(societyRepositoryJPA, times(0)).findSocietyByIdUserCreator(1L);

	}
}
