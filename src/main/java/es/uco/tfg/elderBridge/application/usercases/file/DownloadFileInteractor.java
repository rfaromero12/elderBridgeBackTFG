package es.uco.tfg.elderBridge.application.usercases.file;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.application.mapper.FileMapper;
import es.uco.tfg.elderBridge.application.out.FileOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.File;
import es.uco.tfg.elderBridge.domain.ports.FilePort;
import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class DownloadFileInteractor {
	private FilePort filePort;
	
	public DownloadFileInteractor(FilePort port) {
		this.filePort = port;
	}
	
	public FileOutput doInteractor(FileInput input) {
		if (!filePort.existFileById(input.getIdFile())) {
			throw new DomainException(DomainErrorList.ArchivoNoEncontrado, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			
			
			return FileMapper.toOut(filePort.download(FileMapper.fromInput(input)));
		} catch (Exception e) {
			throw new DomainException(DomainErrorList.DescargaArchivoFallida, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}