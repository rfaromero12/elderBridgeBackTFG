package es.uco.tfg.elderBridge.application.usercases.society;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.SocietyAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.CreateSocietyDTO;

@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo caso de uso Edit Event")
public class EditSocietyInteractorTest {
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	@Mock
	private UserRepositoryJPA userRepositoryJPA;
	
	private EditSocietyInteractor editSocietyInteractor;
	
	private SocietyInput societyInput;
	
	private SocietyEntity societyEntityActual;
	
	private SocietyEntity societyEntityEdited;
	
	private SocietyAdapter societyAdapter;
	
	private UserEntity userEntity;
	
	
	
	@BeforeEach
	void setUp() {
		userEntity = UserEntity.builder()
                .userId(1L)
                .email("nuevo@example.com")
                .password("hashedPassword123")
                .name("Nuevo")
                .surname("Usuario")
                .build();
	societyEntityActual =	SocietyEntity.builder()
		.creator(userEntity)
		.email("ejemplo@gmail.com")
		.societyId(1L)
		.location("locationEjemplo")
		.name("Nuevo")
		.memberships(null)
		.description("description").build();	
	societyEntityEdited =	SocietyEntity.builder()
			.creator(userEntity)
			.email("ejemplo@gmail.com")
			.societyId(1L)
			.location("nuevaLocalizacion")
			.name("nombreEjemplo")
			.memberships(null)
			.description("description").build();	
	societyAdapter = new SocietyAdapter(societyRepositoryJPA, userRepositoryJPA, null);
	societyInput = SocietyInput.builder()
			.idUserCreator(1L)
			.email("ejemplo@gmail.com")
			.idSociety(1L)
			.location("nuevaLocalizacion")
			.name("nombreEjemplo")
			.description("description").build();	
	
	editSocietyInteractor = new EditSocietyInteractor(societyAdapter);
	}
	
	@Test
	@DisplayName("Happy Case")
	void shouldBeEditedSuccesfully() {
		when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.of(societyEntityActual));
		when(societyRepositoryJPA.save(any(SocietyEntity.class))).thenReturn(societyEntityEdited);

		SocietyOutput result = editSocietyInteractor.doInteractor(societyInput);
	
		assertNotNull(result);
        assertEquals("ejemplo@gmail.com", result.getEmail());
        assertEquals("nombreEjemplo", result.getName());
        assertEquals("nuevaLocalizacion", result.getLocation());
        assertEquals("description", result.getDescription());
        assertEquals(1L, result.getIdUserCreator());
        assertNotNull(result.getIdSociety());
   
        verify(societyRepositoryJPA, times(2)).findById(any(Long.class));
        verify(societyRepositoryJPA, times(1)).save(any(SocietyEntity.class));

	}
	
	@Test
	@DisplayName("KO ONG not found")
	void ongNotFound() {
		when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.empty());
		

		DomainException exception = assertThrows(DomainException.class, 
	            () -> editSocietyInteractor.doInteractor(societyInput));
	        
		assertEquals(exception.getName(), DomainErrorList.ONGNoEncontrada.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.ONGNoEncontrada.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	
        verify(societyRepositoryJPA, times(1)).findById(any(Long.class));
        verify(societyRepositoryJPA, times(0)).save(any(SocietyEntity.class));
      
	}
	
	@Test
	@DisplayName("KO Name is already used")
	void nameIsAlreadyUsed() {
		
		when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.of(societyEntityActual));
		
		when(societyRepositoryJPA.findSocietyByName("nombreEjemplo")).thenReturn(Optional.of(societyEntityEdited));

		DomainException exception = assertThrows(DomainException.class, 
	            () -> editSocietyInteractor.doInteractor(societyInput));
	        
		assertEquals(exception.getName(), DomainErrorList.NombreOngEnUso.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.NombreOngEnUso.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	
        verify(societyRepositoryJPA, times(1)).findById(any(Long.class));
        verify(societyRepositoryJPA, times(1)).findSocietyByName("nombreEjemplo");

        verify(societyRepositoryJPA, times(0)).save(any(SocietyEntity.class));
      
	}
}
