package es.uco.tfg.elderBridge.application.usercases.file;

import java.nio.file.Path;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.application.mapper.FileMapper;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.File;
import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.domain.ports.FilePort;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import es.uco.tfg.elderBridge.infrastructure.database.entity.FileEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.storeDocument.StoreFile;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UploadFileInteractor {
	private FilePort filePort;
	private SocietyPort societyPort;
	
	public UploadFileInteractor(FilePort port, SocietyPort societyPort) {
		this.filePort = port;
		this.societyPort = societyPort;
	}
	
	public void doInteractor(FileInput fileInput, Path path) {
		log.info("Subiendo documento nuevo " + fileInput.getName());		
		if (filePort.existFileByNameAndSocietyId(fileInput.getName(), fileInput.getIdSociety())) {
			log.info("Es posible que ya exista el archivo para esa ong");
			throw new DomainException(DomainErrorList.NombreArchivoEnUso, HttpStatus.BAD_REQUEST);
		}
		
				
		Society societyCreator = societyPort.findById(fileInput.getIdSociety());
		if(societyCreator ==null){
			throw new DomainException(DomainErrorList.ONGNoEncontrada, HttpStatus.BAD_REQUEST);
		}
		
		File savedFile =  filePort.save(FileMapper.fromInput(fileInput));
		
		
		if (!filePort.uploadOnStoreFile(savedFile.getName(),path, societyCreator.getName())) {
			filePort.delete(savedFile);
			log.info("El archivo no se ha subido");
			throw new DomainException(DomainErrorList.SubidaArchivoErronea, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		log.info("Documento Subido con exito");
	}
}