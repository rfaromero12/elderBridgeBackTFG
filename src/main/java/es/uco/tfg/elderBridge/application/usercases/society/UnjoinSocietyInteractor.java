package es.uco.tfg.elderBridge.application.usercases.society;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UnjoinSocietyInteractor {
	public UnjoinSocietyInteractor(SocietyPort societyPort, UserPort userPort) {
		this.societyPort = societyPort;
		this.userPort = userPort;
	}

	private SocietyPort societyPort;
	private UserPort userPort;
	
	public void doInteractor(String userEmail, String societyName) {
		log.info("Comenzando proceso de unjoin");
		
		if (!userPort.existUserByEmail(userEmail)) {
			log.info("No hemos encontrado los datos del usuario");
			throw new DomainException(DomainErrorList.UsuarioNoEncontrado, HttpStatus.BAD_REQUEST);
		}		
		
		if (societyPort.findSocietyByName(societyName) == null) {
			log.info("No hemos encontrado los datos de la ONG");
			throw new DomainException(DomainErrorList.ONGNoEncontrada, HttpStatus.BAD_REQUEST);
		}
		societyPort.unjoinSociety(userEmail, societyName);
	}
}
