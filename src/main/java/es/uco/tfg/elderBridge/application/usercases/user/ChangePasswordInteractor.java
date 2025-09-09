package es.uco.tfg.elderBridge.application.usercases.user;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.mapper.UserMapper;
import es.uco.tfg.elderBridge.application.out.UserOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.User;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import es.uco.tfg.elderBridge.domain.service.CryptoService;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ChangePasswordInteractor {
	
	
	private UserPort userPort;
	private CryptoService cryptoService;
	
	public ChangePasswordInteractor(UserPort userPort, CryptoService cryptoService) {
		this.userPort = userPort;
		this.cryptoService = cryptoService;
	}
	
	public UserOutput doInteractor(UserInput userInput){
		User user = userPort.findUserByEmail(userInput.getEmail());
		
		if (userNotFound(user)) {
			throw new DomainException(DomainErrorList.EmailInexistente,HttpStatus.BAD_REQUEST);		
			}
		
		if (passwordNotEquals(userInput.getPassword(), userInput.getConfirmPassword())) {
			throw new DomainException(DomainErrorList.PasswordIncorrecta, HttpStatus.BAD_REQUEST);
		}
		
		if (codesNotEquals(user.getCodeNumber(), userInput.getCodeNumber())) {
			throw new DomainException(DomainErrorList.CodigoNumericoIncorrecto, HttpStatus.BAD_REQUEST);
		}
		
		
		if (user.getCodeExpirationTime().isBefore(LocalDateTime.now())) {
			log.info("El tiempo de validez del codigo ha expirado, intentalo de nuevo");
			userPort.resetCodeNumberValue(user.getEmail());
			throw new DomainException(DomainErrorList.CodigoExpirado, HttpStatus.BAD_REQUEST);
		}
		
		user.setPassword(cryptoService.hashPassword(userInput.getPassword()));
		user.setCodeNumber("");
		user.setCodeExpirationTime(null);
		
		userPort.save(user);
		log.info("La contraseña ha sido modificada con exito");
		
		return UserMapper.toOut(user);
	}
	
	private boolean codesNotEquals(String codeNumberDatabase, String codeNumberInput) {
		return !codeNumberDatabase.equals(codeNumberInput);
	}

	private boolean userNotFound(User user) {
		return user == null;
	}
	
	private boolean passwordNotEquals(String password, String confirmPassword) {
		return !password.equals(confirmPassword);
	}
	
	
	
}
