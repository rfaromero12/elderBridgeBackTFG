package es.uco.tfg.elderBridge.application.usercases.user;

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
public class SignUpUserInteractor{
	private UserPort userPort;
	private CryptoService cryptoService;
	
	public SignUpUserInteractor(UserPort port, CryptoService cryptoService) {
		this.userPort = port;
		this.cryptoService = cryptoService;
	}
	
	public UserOutput doInteractor(UserInput userInput) {
		if(userPort.existUserByEmail(userInput.getEmail())) {
			log.error("El email esta en uso");
			throw new DomainException(DomainErrorList.EmailEnUso, HttpStatus.BAD_REQUEST);
		}
		
		String unHashedPassword = userInput.getPassword();
		userInput.setPassword(cryptoService.hashPassword(unHashedPassword));
		User signedUser = userPort.save(UserMapper.fromInput(userInput)); 
		signedUser.setPassword(unHashedPassword);
		
		return UserMapper.toOut(signedUser);
	}
	
}
