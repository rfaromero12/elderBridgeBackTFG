package es.uco.tfg.elderBridge.application.usercases.user;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.User;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import es.uco.tfg.elderBridge.domain.service.GeneratorValue;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class RecoverPasswordInteractor {
	
	private UserPort userPort;
	private GeneratorValue generatorValue;
	private MailService mailService;
	
	public RecoverPasswordInteractor(UserPort port, GeneratorValue generatorValue, MailService mailService) {
		this.userPort = port;
		this.generatorValue = generatorValue;
		this.mailService = mailService;
	}
	
	public void doInteractor(UserInput userInput) {
		log.info("Recuperando contraseña");
		User user = userPort.findUserByEmail(userInput.getEmail());
		
		if (userNotFound(user)) {
			throw new DomainException(DomainErrorList.EmailInexistente,HttpStatus.BAD_REQUEST);
		}

		String codeNumber = generatorValue.generateCodeNumberVerification();
		LocalDateTime expirationCodeDate = LocalDateTime.now().plusMinutes(15);
		
		
		try {
			mailService.sendEmail(makeRecoverPasswordMessageEmail(user.getEmail(), codeNumber));
		} catch (Exception e) {
			throw new RuntimeException("Error enviando correo electronico");
		}		
		

		try {
			userPort.saveRecoverPasswordVerifications(user.getEmail(), codeNumber, expirationCodeDate);
		} catch (RuntimeException e) {
			throw new DomainException(DomainErrorList.EmailInexistente,HttpStatus.BAD_REQUEST);
		}	
	}
	
	private boolean userNotFound(User user) {
		return user == null;
	}
	
	private SimpleMailMessage makeRecoverPasswordMessageEmail(String userEmail, String codeNumber) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setSentDate(new Date());
		mailMessage.setSubject("Correo para recuperar la contraseña");
		mailMessage.setTo(userEmail);
		mailMessage.setText("Buenas tardes " + userEmail +".\n Le enviamos este correo "
				+ "para que pueda recuperar su contraseña. "
				+ "El código para validar que es usted es: " + codeNumber);
		return mailMessage;
	}
}
