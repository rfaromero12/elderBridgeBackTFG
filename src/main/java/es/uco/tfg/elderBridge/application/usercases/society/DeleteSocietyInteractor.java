package es.uco.tfg.elderBridge.application.usercases.society;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.mapper.SocietyMapper;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DeleteSocietyInteractor {
	private SocietyPort societyPort;
	
	public DeleteSocietyInteractor(SocietyPort port) {
		this.societyPort = port;
	}
	
	public void doInteractor(SocietyInput input) {
		log.info("Borrando ong " + input.getName());
		societyPort.deleteSociety(SocietyMapper.fromInput(input));
		log.info("ONG borrada con exito");

	}
}
