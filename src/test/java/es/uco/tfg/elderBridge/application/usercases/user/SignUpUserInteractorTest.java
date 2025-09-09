package es.uco.tfg.elderBridge.application.usercases.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignUpUserInteractor Integration Tests")
class SignUpUserInteractorTest {

    @Mock
    private UserRepositoryJPA userRepositoryJPA;
    
    
    private MailService mailService;
    
    private CryptoService cryptoService;
    
    private UserAdapter userAdapter;
    private SignUpUserInteractor signUpUserInteractor;

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
        cryptoService = new BCryptServiceImpl();
        mailService = new MailService(new JavaMailSenderImpl());
        userAdapter = new UserAdapter(userRepositoryJPA, null);
        signUpUserInteractor = new SignUpUserInteractor(userAdapter,cryptoService);
    }

    @Test
    @DisplayName("1. Should sign up user successfully with real adapter")
    void shouldSignUpUserSuccessfully() {
        // Given
        when(userRepositoryJPA.findUserByEmail("nuevo@example.com"))
                .thenReturn(Optional.empty()); // Email no existe
        when(userRepositoryJPA.save(any(UserEntity.class)))
                .thenReturn(userEntity);
        //when(cryptoService.hashPassword("password123")).thenReturn("hashedPassword123");
        // When
        UserOutput result = signUpUserInteractor.doInteractor(validUserInput);

        // Then
        assertNotNull(result);
        assertEquals("nuevo@example.com", result.getEmail());
        assertEquals("Nuevo", result.getName());
        assertEquals("Usuario", result.getSurname());
        assertEquals("password123", result.getPassword());
        
        verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
        verify(userRepositoryJPA, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("2. Should throw exception when email already exists in database")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        when(userRepositoryJPA.findUserByEmail("nuevo@example.com"))
                .thenReturn(Optional.of(userEntity)); 

        // When & Then
        DomainException exception = assertThrows(DomainException.class, 
            () -> signUpUserInteractor.doInteractor(validUserInput));
        
        assertEquals(exception.getName(), DomainErrorList.EmailEnUso.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.EmailEnUso.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
        verify(userRepositoryJPA, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("3. Should handle database save failure")
    void shouldHandleDatabaseSaveFailure() {
        // Given
        when(userRepositoryJPA.findUserByEmail("nuevo@example.com"))
                .thenReturn(Optional.empty()); 
        when(userRepositoryJPA.save(any(UserEntity.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> signUpUserInteractor.doInteractor(validUserInput));
        
        assertEquals("Error de base de datos", exception.getMessage());
        verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
        verify(userRepositoryJPA, times(1)).save(any(UserEntity.class));
    }

   
    @Test
    @DisplayName("4. Should handle database connection error")
    void shouldHandleDatabaseConnectionError() {
        // Given
        when(userRepositoryJPA.findUserByEmail("nuevo@example.com"))
                .thenThrow(new RuntimeException("Error de conexión a base de datos"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> signUpUserInteractor.doInteractor(validUserInput));
        
        assertEquals("Error de conexión a base de datos", exception.getMessage());
        verify(userRepositoryJPA, times(1)).findUserByEmail("nuevo@example.com");
        verify(userRepositoryJPA, never()).save(any());
    }
} 