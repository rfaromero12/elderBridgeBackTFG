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
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class EditProfileInteractor {
	private UserPort userPort;
	private CryptoService cryptoService;
	
	public EditProfileInteractor(UserPort port, CryptoService cryptoService) {
		this.userPort = port;
		this.cryptoService = cryptoService;
	}
	
	public UserOutput doInteractor(UserInput userInput) {
		User user = userPort.findUserByEmail(userInput.getEmail());
		if (userNotFound(user)) {
			log.error("Email inexistente " + userInput.getEmail());
			throw new DomainException(DomainErrorList.EmailInexistente,HttpStatus.BAD_REQUEST); 
		}
		
		
		if (emailAlreadyUsed(userInput, user)) {
			log.info("El email informado ya está asociado a otro usuario");
			throw new DomainException(DomainErrorList.EmailEnUso, HttpStatus.BAD_REQUEST);
		}

		if (userWantChangePassword(userInput.getPassword(), userInput.getConfirmPassword()) ) {
			if (passwordNotEquals(userInput, user)) {
				log.info("La contraseña es incorrecta");
				throw new DomainException(DomainErrorList.PasswordIncorrecta, HttpStatus.BAD_REQUEST);
			}
			else {
				user.setPassword(cryptoService.hashPassword(userInput.getConfirmPassword()));
			}
		}
		
		user.setName(userInput.getName());
		user.setSurname(userInput.getSurname());
		
		User editUser = userPort.save(user);
		log.info("Usuario actualizado");		
		return UserMapper.toOut(editUser);
	}

	private boolean userWantChangePassword(String password, String confirmPassword) {
		return StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword);
	}

	/**
	 * @param userInput
	 * @param user
	 * @return
	 */
	private boolean passwordNotEquals(UserInput userInput, User user) {
		return !cryptoService.verifyPassword(userInput.getPassword(),user.getPassword());
	}

	/**
	 * @param userInput
	 * @param user
	 * @return
	 */
	private boolean emailAlreadyUsed(UserInput userInput, User user) {
		return user.getUserId().equals(userInput.getUserId()) && !userInput.getUserId().equals(user.getUserId());
	}
	private boolean userNotFound(User user) {
		return user == null;
	}
}
