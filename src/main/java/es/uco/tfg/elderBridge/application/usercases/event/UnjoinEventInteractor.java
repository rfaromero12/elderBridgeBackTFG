package es.uco.tfg.elderBridge.application.usercases.event;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.Event;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UnjoinEventInteractor {

	
	private EventPort eventPort;
	
	public UnjoinEventInteractor(EventPort eventPort) {
		this.eventPort = eventPort;
	}
	
	public void doInteractor(String userEmail, String eventName) {
		
		Event event = eventPort.findEventByName(eventName);
		
		if (event == null) {
			log.info("No hemos encontrado los daots del evento");
			throw new DomainException(DomainErrorList.EventoNoEncontrado, HttpStatus.BAD_REQUEST);

		}
		
		boolean unJoinSucess =  eventPort.unJoinEvent(userEmail,eventName);
		if (!unJoinSucess) {
			throw new DomainException(DomainErrorList.UsuarioNoEncontrado,HttpStatus.BAD_REQUEST);
		}
		log.info("El usuario ya no participa en el evento");
	}

}
