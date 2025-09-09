package es.uco.tfg.elderBridge.application.usercases.user;

import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.out.UserOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.service.CryptoService;
import es.uco.tfg.elderBridge.infrastructure.crypto.BCryptServiceImpl;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.MemberShipRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;


@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para caso de uso Edit Profile")
public class EditUserProfileInteractorTest {

	  	@Mock
	    private UserRepositoryJPA userRepositoryJPA;
	    @Mock
	    private MemberShipRepositoryJPA memberShipRepositoryJPA;
	    
	    private MailService mailService;
	    
	    
	    private CryptoService cryptoService;
	    
	    private UserAdapter userAdapter;
	    private EditProfileInteractor editUserProfileInteractor;

	    private UserInput validateUserInput;
	    private UserEntity userEntity;
	    
	    @BeforeEach
		void setUp() {
			validateUserInput = UserInput.builder()
						.userId(1L)
			            .email("test@example.com")
			            .password("password123")
			            .name("Nuevo nombre")
			            .surname("Nuevo apellido")
			            .confirmPassword("newPassword")
			            .rol("USER")
			            .build();
			cryptoService = new BCryptServiceImpl();

			
			userEntity = UserEntity.builder()
		                .userId(1L)
		                .email("test@example.com")
			            .password(cryptoService.hashPassword("password123"))
			            .name("Nuevo nombre")
			            .surname("Nuevo apellido")
		                .build();
		    mailService = new MailService(new JavaMailSenderImpl());

			userAdapter = new UserAdapter(userRepositoryJPA, memberShipRepositoryJPA);
			editUserProfileInteractor = new EditProfileInteractor(userAdapter, cryptoService);
	    }
	    
	    @Test
	    @DisplayName("Happy Case User Edit Ok")
	    void shouldUserBeEditedSuccessfully() {

	    	when(userRepositoryJPA.findUserByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
	    	when(userRepositoryJPA.save(any())).thenReturn(userEntity);

	    	UserOutput result = editUserProfileInteractor.doInteractor(validateUserInput);
	    	
	    	assertEquals(validateUserInput.getName(), result.getName());
	    	assertEquals(validateUserInput.getSurname(), result.getSurname());
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("test@example.com");
	    	verify(userRepositoryJPA, times(1)).save(any(UserEntity.class));
	    	
	    }
	    
	    @Test
	    @DisplayName("email inexistente")
	    void user_not_be_found() {
	    	when(userRepositoryJPA.findUserByEmail("test@example.com")).thenReturn(Optional.empty());	    	
	    	DomainException result = assertThrows(DomainException.class ,()->editUserProfileInteractor.doInteractor(validateUserInput));
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("test@example.com");
	    	verify(userRepositoryJPA, times(0)).save(userEntity);
	    	assertEquals(DomainErrorList.EmailInexistente.getName(), result.getName());
	    	assertEquals(DomainErrorList.EmailInexistente.getDescription(), result.getDescripcion());
	    	assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
	    	
	    	

	    }
	    
	    @Test
	    @DisplayName("old password is wrong")
	    void old_password_is_wrong() {
	    	
	    	userEntity.setPassword(cryptoService.hashPassword("password_incorrecta"));
	    	when(userRepositoryJPA.findUserByEmail("test@example.com")).thenReturn(Optional.of(userEntity));	    	
	    	DomainException result = assertThrows(DomainException.class ,()->editUserProfileInteractor.doInteractor(validateUserInput));
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("test@example.com");
	    	verify(userRepositoryJPA, times(0)).save(userEntity);
	    	assertEquals(DomainErrorList.PasswordIncorrecta.getName(), result.getName());
	    	assertEquals(DomainErrorList.PasswordIncorrecta.getDescription(), result.getDescripcion());
	    	assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
	    	
	    	

	    }
}
