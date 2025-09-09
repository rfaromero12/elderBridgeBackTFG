package es.uco.tfg.elderBridge.application.usercases.society;

import java.util.List;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.mapper.SocietyMapper;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ShowListSocietyInteractor {
	private SocietyPort societyPort;
	
	public ShowListSocietyInteractor(SocietyPort port) {
		this.societyPort = port;
	}
	
	public List<SocietyOutput> doInteractor(String userEmail) {
		log.info("Buscando OngS en las que el usuario esta subscrito");
		return SocietyMapper.toOutList(societyPort.showSubSociety(userEmail));
	}
}
