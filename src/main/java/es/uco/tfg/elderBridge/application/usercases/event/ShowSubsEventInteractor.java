package es.uco.tfg.elderBridge.application.usercases.event;

import java.util.List;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.mapper.EventMapper;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ShowSubsEventInteractor {

	private EventPort eventPort;
	
	public ShowSubsEventInteractor(EventPort eventPort) {
		this.eventPort = eventPort;
	}
	
	public List<EventOutput> doInteractor(String userEmail) {
		log.info("Buscando los eventos a los que esta subscrito el usuario " + userEmail);
		return EventMapper.toOutList(eventPort.showSubsEvent(userEmail));
	}
	
}
