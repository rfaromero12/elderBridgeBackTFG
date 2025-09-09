package es.uco.tfg.elderBridge.application.usercases.event;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.application.mapper.EventMapper;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DeleteEventInteractor {
	private EventPort eventPort;
	
	public DeleteEventInteractor(EventPort eventPort) {
		this.eventPort = eventPort;
	}
	
	public void doInteractor(EventInput event) {
		log.info("Borrando el evento con nombre " + event.getName());
		
		try {
			eventPort.deleteEvent(EventMapper.fromInput(event),makeDeleteEventMessageEmail(event.getName()));
			log.info("Borrado con exito");

		} catch (RuntimeException e) {
			log.error("Error en el borrado");
			throw new DomainException(DomainErrorList.EventoNoEncontrado, HttpStatus.BAD_REQUEST );
		}
		
		
	}
	
	private SimpleMailMessage makeDeleteEventMessageEmail(String eventName) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setSentDate(new Date());
		mailMessage.setSubject("Cancelación del evento " + eventName);
		mailMessage.setText("Buenas tardes "+".\n Le enviamos este correo "
				+ " para avisar de que el evento " + eventName + " ha sido cancelado. Disculpen las molestias");
		return mailMessage;
	}
}
