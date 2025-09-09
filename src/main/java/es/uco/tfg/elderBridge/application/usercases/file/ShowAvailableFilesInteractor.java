package es.uco.tfg.elderBridge.application.usercases.file;

import java.util.List;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.application.mapper.FileMapper;
import es.uco.tfg.elderBridge.application.out.FileOutput;
import es.uco.tfg.elderBridge.domain.ports.FilePort;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ShowAvailableFilesInteractor {
	private FilePort filePort;
	
	public ShowAvailableFilesInteractor(FilePort port) {
		this.filePort = port;
	}
	
	public List<FileOutput> doInteractor(FileInput input) {
		log.info("Obteniendo archivos disponibles de la ong " + input.getName());
		return  FileMapper.toOutList(filePort.getFiles(FileMapper.fromInput(input)));

	}
}