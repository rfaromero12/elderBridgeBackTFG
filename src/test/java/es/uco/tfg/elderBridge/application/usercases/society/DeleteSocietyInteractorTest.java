package es.uco.tfg.elderBridge.application.usercases.society;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.usercases.event.DeleteEventInteractor;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.SocietyAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.CreateSocietyDTO;

@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo caso de uso Delete Society")
public class DeleteSocietyInteractorTest {

	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	@Mock
	private UserRepositoryJPA userRepositoryJPA;
	
	private DeleteSocietyInteractor deleteSocietyInteractor;
	
	private SocietyInput societyInput;
	
	private SocietyEntity societyEntity;
	
	private SocietyAdapter societyAdapter;
	
	private UserEntity userEntity;
	
	
	
	@BeforeEach
	void setUp() {
		userEntity = UserEntity.builder()
                .userId(1L)
                .email("test@example.com")
	            .password("sddssds")
	            .name("Test")
	            .surname("User")
	            .createdOrganization(societyEntity)
                .build();

		societyEntity =	SocietyEntity.builder()
				.creator(userEntity)
				.email("ejemplo@gmail.com")
				.societyId(null)
				.location("locationEjemplo")
				.name("nombreEjemplo")
				.memberships(null)
				.description("description").build();	
		userEntity.setCreatedOrganization(societyEntity);
		 
	societyAdapter = new SocietyAdapter(societyRepositoryJPA, userRepositoryJPA, null);
	societyInput = SocietyInput.builder()
			.idUserCreator(null)
			.email("ejemplo@gmail.com")
			.idSociety(1L)
			.location("locationEjemplo")
			.name("nombreEjemplo")
			.description("description").build();	
	
	deleteSocietyInteractor = new DeleteSocietyInteractor(societyAdapter);
	}
	
	 @Test
	   	@DisplayName("1. Happy Case")
	    void shouldDeleteSocietySuccessfully() {
	    	when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.of(societyEntity));
	    	
	    	deleteSocietyInteractor.doInteractor(societyInput);
	    	
	    	verify(societyRepositoryJPA,times(1)).findById(1L);
	    	verify(societyRepositoryJPA,times(1)).delete(any(SocietyEntity.class));
	    }
	    
	    @Test
	    @DisplayName("2. KO Case")
	    void shouldNotDeleteUserSuccessfully() {
	    	when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.empty());
	    	
	    	assertThrows(RuntimeException.class, ()-> deleteSocietyInteractor.doInteractor(societyInput));
	    	
	    	verify(societyRepositoryJPA,times(1)).findById(1L);
	    	
	    }
	
}
