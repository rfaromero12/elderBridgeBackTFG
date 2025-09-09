package es.uco.tfg.elderBridge.application.usercases.user;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.mapper.UserMapper;
import es.uco.tfg.elderBridge.application.out.UserOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.User;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import es.uco.tfg.elderBridge.domain.service.AuthService;
import es.uco.tfg.elderBridge.domain.service.CryptoService;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class LoginUserInteractor {
	private UserPort userPort;
	private AuthService authService;
	private CryptoService cryptoService;
	
	public LoginUserInteractor(UserPort port, AuthService authenticationPort, CryptoService cryptoService) {
		this.userPort = port;
		this.authService =authenticationPort;
		this.cryptoService = cryptoService;
	}
	
	public UserOutput doInteractor(UserInput userInput){
		String token =authService.getToken(new org.springframework.security.core.userdetails.User(userInput.getEmail(), userInput.getPassword(), Collections.emptyList())); 
		
		User user =  userPort.findUserByEmail(userInput.getEmail());
		if (userNotFound(user)) {
			log.info("El email informado no esta asociado a ningun usuario");
			throw new DomainException(DomainErrorList.UsuarioNoEncontrado, HttpStatus.BAD_REQUEST);
		}

		if (passwordNotEquals(userInput,user)) {
			log.info("La contraseña es incorrecta");
			throw new DomainException(DomainErrorList.PasswordIncorrecta, HttpStatus.BAD_REQUEST);
		}	
		user.setPassword(userInput.getPassword());
		
		return UserMapper.toOut(user, token);
	}

	private boolean userNotFound(User user) {
		return user == null;
	}
	/**
	 * @param userInput
	 * @param user
	 * @return
	 */
	private boolean passwordNotEquals(UserInput userInput, User user) {
		return !cryptoService.verifyPassword(userInput.getPassword(),user.getPassword());
	}
}
