package es.uco.tfg.elderBridge.application.usercases.society;

import java.util.List;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.mapper.SocietyMapper;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ShowAvailableSocietyInteractor {
private SocietyPort societyPort;
	
	public ShowAvailableSocietyInteractor(SocietyPort port) {
		this.societyPort = port;
	}
	
	public List<SocietyOutput> doInteractor(String idUserCreator) {
		log.info("Buscando todas las ONG disponibles para " + idUserCreator);
		return SocietyMapper.toOutList(societyPort.showAvailableSocieties(idUserCreator));
	}
}
