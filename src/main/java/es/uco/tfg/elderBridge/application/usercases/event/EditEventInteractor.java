package es.uco.tfg.elderBridge.application.usercases.event;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.application.mapper.EventMapper;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.Event;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;
import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class EditEventInteractor {
	private EventPort eventPort;
	
	public EditEventInteractor(EventPort eventPort) {
		
		this.eventPort = eventPort;
	}
	
	public EventOutput doInteractor(EventInput eventInput) {	
		
		Event event = eventPort.findById(eventInput.getEventId());
		if(event ==null) {
			throw new DomainException(DomainErrorList.EventoNoEncontrado, HttpStatus.BAD_REQUEST);
		}
		
		
		
		if (eventInput.getEventDate().isBefore(LocalDateTime.now().plusDays(1))) {
			log.error("La fecha a modificar del evento debe ser 1 dia posterior al actual");
			throw new DomainException(DomainErrorList.FechasInvalidas, HttpStatus.BAD_REQUEST);

		}
		if (!eventInput.getName().equals(event.getName()) && 
				eventPort.findEventByName(eventInput.getName()) != null) {
			log.error("El nombre está en uso");
			throw new DomainException(DomainErrorList.NombreEventoEnUso, HttpStatus.BAD_REQUEST);
		}
		
		
		boolean dateChanged = changeDateValidate(eventInput.getEventDate(), event.getEventDate());
		LocalDateTime oldDate = event.getEventDate();	
		event.setDescription(eventInput.getDescription());
		event.setEventDate(eventInput.getEventDate());
		event.setEventLocation(eventInput.getEventLocation());

		eventPort.update(event);
		
		if (dateChanged) {
			eventPort.sendChangeEventDateEmail(event.getEventId(),
					makeChangeEventDateMessageEmail(event.getName(),oldDate,event.getEventDate()));
		}
		
		log.info("ONG actualizado");
		
		return EventMapper.toOut(event);
	}

	private boolean changeDateValidate(LocalDateTime actualDate, LocalDateTime newDate) {
		return !actualDate.isEqual(newDate);
	}
	
	private SimpleMailMessage makeChangeEventDateMessageEmail(String eventName, LocalDateTime oldDate, LocalDateTime newDate) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setSentDate(new Date());
		mailMessage.setSubject("Cambio de fecha del evento " + eventName);
		mailMessage.setText("Buenas tardes "+".\n Le enviamos este correo "
				+ " para avisar de que el evento " + eventName + " ha sido cambiado de fecha. Ha pasado del dia " + oldDate.toString()  
				+ " a el dia " + newDate.toString());
		return mailMessage;
	}
}