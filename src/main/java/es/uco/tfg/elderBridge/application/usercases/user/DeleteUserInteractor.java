package es.uco.tfg.elderBridge.application.usercases.user;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DeleteUserInteractor {
	
	private UserPort userPort;
	
	public DeleteUserInteractor(UserPort port) {
		this.userPort = port;
	}
	
	public void doInteractor(UserInput input) {
		log.info("Borrando cuenta del usuario " + input.getEmail());
		userPort.deleteAccount(input.getEmail());
	}
}
