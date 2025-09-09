package es.uco.tfg.elderBridge.application.usercases.user;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.ArgumentMatchers.any;


import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.out.UserOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.service.AuthService;
import es.uco.tfg.elderBridge.domain.service.CryptoService;
import es.uco.tfg.elderBridge.infrastructure.crypto.BCryptServiceImpl;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.MemberShipRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;
import es.uco.tfg.elderBridge.infrastructure.security.JwtService;



@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para caso de uso Login")
public class LoginUserInteractorTest {

	  // Mocks para infraestructura
    @Mock
    private UserRepositoryJPA userRepositoryJPA;
    @Mock
    private MemberShipRepositoryJPA memberShipRepositoryJPA;
    
    private MailService mailService;
    
    private CryptoService cryptoService;
    
    private UserAdapter userAdapter;
    private LoginUserInteractor loginUserInteractor;

    private UserInput validateUserInput;
    private UserEntity userEntity;
    private boolean validado = true;
    @Mock
    private AuthService authService;
    
	@BeforeEach
	void setUp() {
		validateUserInput = UserInput.builder()
		            .email("test@example.com")
		            .password("password123")
		            .name("Test")
		            .surname("User")
		            .rol("USER")
		            .build();
		 
		cryptoService = new BCryptServiceImpl();
		
		 userEntity = UserEntity.builder()
	                .userId(1L)
	                .email("test@example.com")
		            .password(cryptoService.hashPassword("password123"))
		            .name("Test")
		            .surname("User")
	                .build();
	    mailService = new MailService(new JavaMailSenderImpl());
		userAdapter = new UserAdapter(userRepositoryJPA, memberShipRepositoryJPA);
		loginUserInteractor = new LoginUserInteractor(userAdapter,authService, cryptoService);
	}
	
	
	@Test
	@DisplayName("1. Happy Case Login OK")
	void shouldLoginSuccesfully() {
		//given
		when(userRepositoryJPA.findUserByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
		when(authService.getToken(any(UserDetails.class))).thenReturn("token");
		//when
		UserOutput result = loginUserInteractor.doInteractor(validateUserInput);
		
		//then
		assertNotNull(result);
		assertEquals(validateUserInput.getEmail(), result.getEmail());
		assertEquals(validateUserInput.getName(), result.getName());
		assertEquals(validateUserInput.getSurname(), result.getSurname());
		assertNotNull(result.getToken());
		
		verify(userRepositoryJPA, times(1)).findUserByEmail("test@example.com");
		verify(authService, times(1)).getToken(any(UserDetails.class));


	}
	
	@Test
	@DisplayName("2. Email not exists KO")
	void emailNotExists() {
		//given
		when(userRepositoryJPA.findUserByEmail("test@example.com")).thenReturn(Optional.empty());
		when(authService.getToken(any(UserDetails.class))).thenReturn("token");
		
		//when
		DomainException exception= assertThrows(DomainException.class,
				()->loginUserInteractor.doInteractor(validateUserInput));
		
		//then
		assertNotNull(exception);
		assertEquals(exception.getName(), DomainErrorList.UsuarioNoEncontrado.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.UsuarioNoEncontrado.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		
		verify(userRepositoryJPA, times(1)).findUserByEmail("test@example.com");
		
	}
	
	@Test
	@DisplayName("3. Password Not Equals KO")
	void passwordsNotEquals() {
		//given
		validateUserInput.setPassword("abcd");
		when(userRepositoryJPA.findUserByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
		when(authService.getToken(any(UserDetails.class))).thenReturn("token");

		//when
		DomainException exception= assertThrows(DomainException.class,
				()->loginUserInteractor.doInteractor(validateUserInput));
		
		//then
		assertNotNull(exception);
		assertEquals(exception.getName(), DomainErrorList.PasswordIncorrecta.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.PasswordIncorrecta.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
		verify(userRepositoryJPA, times(1)).findUserByEmail("test@example.com");
	}
	
}
