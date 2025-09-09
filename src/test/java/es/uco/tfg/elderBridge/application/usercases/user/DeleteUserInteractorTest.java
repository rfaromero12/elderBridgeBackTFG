package es.uco.tfg.elderBridge.application.usercases.user;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.MemberShipRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para caso de uso Delete")
public class DeleteUserInteractorTest {
	  @Mock
	    private UserRepositoryJPA userRepositoryJPA;
	    @Mock
	    private MemberShipRepositoryJPA memberShipRepositoryJPA;
	    
	    private UserAdapter userAdapter;
	    private DeleteUserInteractor deleteUserInteractor;

	    // Datos de prueba
	    private UserInput validateUserInput;
	    private UserEntity userEntity;
	    
	    @BeforeEach
		void setUp() {
			validateUserInput = UserInput.builder()
			            .email("test@example.com")
			            .password("password123")
			            .name("Test")
			            .surname("User")
			            .rol("USER")
			            .build();
			  userEntity = UserEntity.builder()
		                .userId(1L)
		                .email("test@example.com")
			            .password("password123")
			            .name("Test")
			            .surname("User")
		                .build();
			
			userAdapter = new UserAdapter(userRepositoryJPA, memberShipRepositoryJPA);
			deleteUserInteractor = new DeleteUserInteractor(userAdapter);
	    }
	    
	    @Test
	    @DisplayName("1. Happy Case")
	    void shouldDeleteUserSuccessfully() {
	    	when(userRepositoryJPA.deleteByEmail("test@example.com")).thenReturn(1);
	    	
	    	deleteUserInteractor.doInteractor(validateUserInput);
	    	
	    	verify(userRepositoryJPA,times(1)).deleteByEmail("test@example.com");
	    }
	    
	    @Test
	    @DisplayName("2. KO Case")
	    void userNotDeleteSuccessfully() {
	    	when(userRepositoryJPA.deleteByEmail("test@example.com")).thenReturn(0);
	    	
	    	RuntimeException exception =assertThrows(RuntimeException.class, ()-> deleteUserInteractor.doInteractor(validateUserInput));
	    	
	    	verify(userRepositoryJPA,times(1)).deleteByEmail("test@example.com");
	        assertEquals("El usuario con email " + "test@example.com" + " no ha sido eliminado", exception.getMessage());
	    	
	    }
}
