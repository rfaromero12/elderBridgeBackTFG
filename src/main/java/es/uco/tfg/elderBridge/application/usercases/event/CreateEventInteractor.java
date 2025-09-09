package es.uco.tfg.elderBridge.application.usercases.event;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.application.mapper.EventMapper;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.Event;
import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.domain.models.User;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.ParticipantEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CreateEventInteractor {
	
	private EventPort eventPort;

	
	public CreateEventInteractor(EventPort eventPort) {
		this.eventPort = eventPort;
	
	}
	
	public EventOutput doInteractor(EventInput eventInput) {
		log.info("Creando evento nuevo");
		if(eventPort.findEventByName(eventInput.getName()) != null) {
			log.error("El nombre ya esta en uso");
			throw new DomainException(DomainErrorList.NombreEventoEnUso, HttpStatus.BAD_REQUEST);
		}
		if (eventDayIsBeforeToday(eventInput.getEventDate())) {
			log.error("La fecha del evento no puede ser anterior a la fecha actual");
			throw new DomainException(DomainErrorList.FechasInvalidas, HttpStatus.BAD_REQUEST);

		}
		
		
		Event newEvent = eventPort.createEvent(EventMapper.fromInput(eventInput));
		
		return EventMapper.toOut(newEvent);
	}

	private boolean eventDayIsBeforeToday(LocalDateTime eventDate) {
		return eventDate.isBefore(LocalDateTime.now());
	}
}
