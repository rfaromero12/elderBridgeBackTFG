 package es.uco.tfg.elderBridge.application.usercases.user;

import java.util.List;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.mapper.SocietyMapper;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ShowMemberSocietyInteractor {
	private UserPort userPort;
	
	public ShowMemberSocietyInteractor(UserPort port) {
		this.userPort = port;
	}
	
	public List<SocietyOutput> doInteractor(UserInput input) {
		log.info("Buscando todas las ONGs de este usuario: " +  input.getEmail());
		return SocietyMapper.toOutList(userPort.showMemberSociety(input.getEmail()));
	}
	
}
