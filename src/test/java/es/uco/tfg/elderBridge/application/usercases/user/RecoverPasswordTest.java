package es.uco.tfg.elderBridge.application.usercases.user;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.service.GeneratorValue;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.UserAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;
@ExtendWith(MockitoExtension.class)
@DisplayName("RecoverPasswordTest")
public class RecoverPasswordTest {
		@Mock
	    private UserRepositoryJPA userRepositoryJPA;
	    
	    private MailService mailService;
;
	    @Mock 
	    private JavaMailSender javaMailSender;
	    @Mock 
	    private GeneratorValue generatorValue;
	    private UserAdapter userAdapter;
	    private RecoverPasswordInteractor recoverPasswordInteractor;

	    private UserInput validUserInput;
	    private UserEntity userEntity;

	    @BeforeEach
	    void setUp() {
	        validUserInput = UserInput.builder()
	                .email("nuevo@example.com")
	                .password("password123")
	                .name("Nuevo")
	                .surname("Usuario")
	                .rol("USER")
	                .build();

	        userEntity = UserEntity.builder()
	                .userId(1L)
	                .email("nuevo@example.com")
	                .password("hashedPassword123")
	                .name("Nuevo")
	                .surname("Usuario")
	                .build();
	        mailService = new MailService(javaMailSender);
	        userAdapter = new UserAdapter(userRepositoryJPA, null);
	        recoverPasswordInteractor = new RecoverPasswordInteractor(userAdapter,generatorValue,mailService);
	    }
	    
	    @Test
	    @DisplayName("Happy Case Recover Password")
	    void should_recover_ok() {
	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
	    	when(generatorValue.generateCodeNumberVerification()).thenReturn("222222");
	    	recoverPasswordInteractor.doInteractor(validUserInput);
	    	
	    	verify(userRepositoryJPA, times(2)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(1)).save(userEntity);
	    	verify(generatorValue, times(1)).generateCodeNumberVerification();



	    }
	    
	    @Test
	    @DisplayName("Email not Found")
	    void email_not_found() {

	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.empty());
	    	DomainException result =  assertThrows(DomainException.class,()->recoverPasswordInteractor.doInteractor(validUserInput));
	    	
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(0)).save(userEntity);
	    	verify(generatorValue, times(0)).generateCodeNumberVerification();

	    	assertEquals(DomainErrorList.EmailInexistente.getName(), result.getName());
	    	assertEquals(DomainErrorList.EmailInexistente.getDescription(), result.getDescripcion());
	    	assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
	    
	    	
	    }
	    
	    @Test
	    @DisplayName("Error sending mail")
	    void send_mail_not_be_success() {
	        
	    	when(userRepositoryJPA.findUserByEmail("nuevo@example.com")).thenReturn(Optional.of(userEntity));
	    	when(generatorValue.generateCodeNumberVerification()).thenReturn("222222");
	    	
	    	doThrow(new RuntimeException()).when(javaMailSender).send(any(SimpleMailMessage.class));
	    	
	    	RuntimeException result =  assertThrows(RuntimeException.class,()->recoverPasswordInteractor.doInteractor(validUserInput));
	    	
	    	
	    	verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
	    	verify(userRepositoryJPA, times(0)).save(userEntity);
	    	verify(generatorValue, times(1)).generateCodeNumberVerification();

	    	assertEquals(RuntimeException.class, result.getClass());
	    }

}
