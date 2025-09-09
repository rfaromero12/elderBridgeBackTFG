package es.uco.tfg.elderBridge.application.usercases.society;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.mapper.SocietyMapper;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class EditSocietyInteractor {

	private SocietyPort societyPort;
	
	public EditSocietyInteractor(SocietyPort port) {
		this.societyPort = port;
	}
	
	public SocietyOutput doInteractor(SocietyInput societyInput) {

		Society society = societyPort.findById(societyInput.getIdSociety());
		if (society == null) {
			throw new DomainException(DomainErrorList.ONGNoEncontrada, HttpStatus.BAD_REQUEST);
		}
		
		
		if (!society.getName().equals(societyInput.getName()) && societyPort.findSocietyByName(societyInput.getName()) !=null) {
			log.info("El nombre informado ya está asociado a una ONG");
			throw new DomainException(DomainErrorList.NombreOngEnUso, HttpStatus.BAD_REQUEST);
		}

		return SocietyMapper.toOut(societyPort.update(SocietyMapper.fromInput(societyInput)));
	}
}
