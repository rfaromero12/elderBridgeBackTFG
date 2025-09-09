package es.uco.tfg.elderBridge.application.usercases.event;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import es.uco.tfg.elderBridge.domain.ports.UserPort;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JoinEventInteractor {
	public JoinEventInteractor(EventPort eventPort, UserPort userPort) {
		this.eventPort = eventPort;
		this.userPort = userPort;
	}

	private EventPort eventPort;
	private UserPort userPort;

	
	public void doInteractor(String userEmail, String eventName) {		
		log.info("Comenzando proceso de join");

		if (!userPort.existUserByEmail(userEmail)) {
			log.info("No hemos encontrado los datos del usuario");
			throw new DomainException(DomainErrorList.UsuarioNoEncontrado, HttpStatus.BAD_REQUEST);
		}

		if (eventPort.findEventByName(eventName) == null) {
			log.info("No hemos encontrado los daots del evento");
			throw new DomainException(DomainErrorList.EventoNoEncontrado, HttpStatus.BAD_REQUEST);

		}
		
		eventPort.joinEvent(userEmail, eventName);

		log.info("El usuario ya participa en el evento");
	}
}
