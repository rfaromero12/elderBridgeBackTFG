package es.uco.tfg.elderBridge.application.usercases.society;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.mapper.SocietyMapper;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CreateSocietyInteractor {
	
	private SocietyPort societyPort;
	private UserPort userPort;
	public CreateSocietyInteractor(SocietyPort port, UserPort userPort) {
		this.userPort = userPort;
		this.societyPort = port;
	}
	
	public SocietyOutput doInteractor(SocietyInput societyInput) {
		if(societyPort.findSocietyByName(societyInput.getName()) != null) {
			log.error("El nombre ya esta en uso");
			throw new DomainException(DomainErrorList.NombreOngEnUso, HttpStatus.BAD_REQUEST);
		}
		
		if (societyPort.findSocietyByEmail(societyInput.getEmail())!= null) {
			log.error("El usuario ya es dueño de una ong");
			throw new DomainException(DomainErrorList.EmailEnUso, HttpStatus.BAD_REQUEST);
		}
		
		if (societyPort.findSocietyByIdUserCreator(societyInput.getIdUserCreator())!= null) {
			log.error("El email esta siendo usado por otra ong");
			throw new DomainException(DomainErrorList.LimiteOngsPorUsuarioSuperado, HttpStatus.BAD_REQUEST);
		}
		
		if (userPort.findUserById(societyInput.getIdUserCreator()) == null) {
			log.error("No se han podido obtener los datos del usuario creador de la ong");
			throw new DomainException(DomainErrorList.UsuarioNoEncontrado, HttpStatus.BAD_REQUEST);

		}
				
		return SocietyMapper.toOut(societyPort.createSociety(SocietyMapper.fromInput(societyInput)));
	}
}
