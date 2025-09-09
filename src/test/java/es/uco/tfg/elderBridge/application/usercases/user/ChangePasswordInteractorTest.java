package es.uco.tfg.elderBridge.application.usercases.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.out.UserOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.service.CryptoService;
import es.uco.tfg.elderBridge.infrastructure.crypto.BCryptServiceImpl;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Change password tests")
public class ChangePasswordInteractorTest {
		@Mock
	    private UserRepositoryJPA userRepositoryJPA;
	    
	    @Mock
	    private MailService mailService;
	    
	    
	    private CryptoService cryptoService;
	    
	    private UserAdapter userAdapter;
	    private ChangePasswordInteractor changePasswordInteractor;
		Clock clock = Clock.fixed( Instant.parse("2026-08-10T10:15:30Z"), ZoneId.of("UTC"));

	    private UserInput validUserInput;
	    private UserEntity userEntity;
	    private String codeNumber ="1234";
	    private String newPassword="aaaa";
	    
	    @BeforeEach
	    void setUp() {
	        validUserInput = UserInput.builder()
	        		.userId(1L)
	                .email("nuevo@example.com")
	                .password("password123")
	                .name("Nuevo")
	                .surname("Usuario")
	                .rol("USER")
	                .confirmPassword("password123")
	                .codeNumber(codeNumber)
	                .build();
			cryptoService = new BCryptServiceImpl();

	        userEntity = UserEntity.builder()
	                .userId(1L)
	                .email("nuevo@example.com")
	                .password(cryptoService.hashPassword("password123"))
	                .name("Nuevo")
	                .surname("Usuario")
	                .codeNumber(codeNumber)
	                .codeExpirationTime(LocalDateTime.now().plusMinutes(3))
	                .build();

	        userAdapter = new UserAdapter(userRepositoryJPA, null);
	        changePasswordInteractor = new ChangePasswordInteractor(userAdapter, cryptoService);
	    }
	    
	    @Test
	    @DisplayName("Happy Case Change Password Ok")
	    void shouldUserBeEditedSuccessfully() {

	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
	    	when(userRepositoryJPA.save(any())).thenReturn(userEntity);
	    	UserOutput result = changePasswordInteractor.doInteractor(validUserInput);
	    	
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(1)).save(any(UserEntity.class));
	    	
	    }
	    
	    @Test
	    @DisplayName("user not found by email")
	    void userNotFoundByEmail() {
	    	
	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.empty());
	    	
			DomainException exception = assertThrows(DomainException.class, 
		            () -> changePasswordInteractor.doInteractor(validUserInput));
		
			assertEquals(exception.getName(), DomainErrorList.EmailInexistente.getName());
			assertEquals(exception.getDescripcion(), DomainErrorList.EmailInexistente.getDescription());
			assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	  
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(0)).save(userEntity);
	    	
	    }
	    
	    @Test
	    @DisplayName("Password not equals to confirmPassword")
	    void passwordsAreNotEquals() {
	    	validUserInput.setConfirmPassword("badPassword");
	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
	    	
	    	DomainException exception = assertThrows(DomainException.class, 
		            () -> changePasswordInteractor.doInteractor(validUserInput));
		
			assertEquals(exception.getName(), DomainErrorList.PasswordIncorrecta.getName());
			assertEquals(exception.getDescripcion(), DomainErrorList.PasswordIncorrecta.getDescription());
			assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	  
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(0)).save(userEntity);
	    	
	    }
	    
	    @Test
	    @DisplayName("CodeNumber is wrong")
	    void codeNumberIsWrong() {
	    	validUserInput.setCodeNumber("1111");
	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
	    	
	    	DomainException exception = assertThrows(DomainException.class, 
		            () -> changePasswordInteractor.doInteractor(validUserInput));
		
			assertEquals(exception.getName(), DomainErrorList.CodigoNumericoIncorrecto.getName());
			assertEquals(exception.getDescripcion(), DomainErrorList.CodigoNumericoIncorrecto.getDescription());
			assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	  
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(0)).save(userEntity);
	    	
	    }
	    
	    @Test
	    @DisplayName("Time to change password was expired")
	    void timeToChangePasswordWasExpired() {
	    	userEntity.setCodeExpirationTime(LocalDateTime.now().minusMinutes(3));
	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
	    	
	    	DomainException exception = assertThrows(DomainException.class, 
		            () -> changePasswordInteractor.doInteractor(validUserInput));
		
			assertEquals(exception.getName(), DomainErrorList.CodigoExpirado.getName());
			assertEquals(exception.getDescripcion(), DomainErrorList.CodigoExpirado.getDescription());
			assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	  
	    	
	    	verify(userRepositoryJPA, times(2)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(1)).save(userEntity);
	    	
	    }
}
