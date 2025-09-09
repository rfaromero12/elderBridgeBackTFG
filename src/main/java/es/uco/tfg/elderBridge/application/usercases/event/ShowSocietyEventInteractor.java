package es.uco.tfg.elderBridge.application.usercases.event;

import java.util.List;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.mapper.EventMapper;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ShowSocietyEventInteractor {

	private EventPort eventPort;
	
	public ShowSocietyEventInteractor(EventPort eventPort) {
		this.eventPort = eventPort;
	}
	
	public List<EventOutput> doInteractor(String name) {
		log.info("Buscando los eventos de la entidad " + name);
		return EventMapper.toOutList(eventPort.showSocietyEvent(name));
	}
	
}
	