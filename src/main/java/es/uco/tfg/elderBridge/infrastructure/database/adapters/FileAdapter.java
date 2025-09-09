package es.uco.tfg.elderBridge.infrastructure.database.adapters;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.models.File;
import es.uco.tfg.elderBridge.domain.ports.FilePort;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers.FileMapper;
import es.uco.tfg.elderBridge.infrastructure.database.entity.FileEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.FilesEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.storeDocument.StoreFile;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class FileAdapter implements FilePort{
	
	
	private FilesEntityRepositoryJPA fileRepositoryJPA;
	private StoreFile storeFile;
	private SocietyRepositoryJPA societyRepositoryJPA;
	public FileAdapter(FilesEntityRepositoryJPA fileRepositoryJPA,
			StoreFile storeFile,
			SocietyRepositoryJPA societyRepositoryJPA) {
		this.fileRepositoryJPA = fileRepositoryJPA;
		this.storeFile = storeFile;
		this.societyRepositoryJPA = societyRepositoryJPA;
	}

	@Override
	public List<File> getFiles(File file) {
		List<FileEntity> fileList = fileRepositoryJPA.findFilesBySociety(file.getIdSociety());
		if (Objects.isNull(fileList)) {
			return Collections.emptyList();
		}
		
		log.info("La entidad tiene " + fileList.size() + "ficheros");
		return FileMapper.toOutList(fileList);
	}

	@Override
	public File download(File file) {
		FileEntity fileEntity = fileRepositoryJPA.findById(file.getIdFile()).get();
		try {
			byte[] fileContent =  storeFile.download(fileEntity.getCreator().getName().toLowerCase(), fileEntity.getName());
			log.info("El archivo ha sido descargado con exito");
			File downloadFile = FileMapper.fromSchemaToDomain(fileEntity);
			downloadFile.setFileContent(fileContent);
			return downloadFile;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}



	@Override
	public File findFileByName(String name) {
		FileEntity fileEntity = fileRepositoryJPA.findFileByName(name).orElseGet(()->null);
		return FileMapper.fromSchemaToDomain(fileEntity);
	}
	


	@Override
	public boolean existFileByNameAndSocietyId(String name, Long idSociety) {
		log.info("Buscando si ya existe el fichero");
		return fileRepositoryJPA.existsByNameAndSocietyId(name, idSociety);
	}



	@Override
	public void delete(File savedFile) {
		fileRepositoryJPA.delete(FileMapper.fromDomainToSchema(savedFile));
	}



	@Override
	public File save(File file) {
		SocietyEntity societyCreator = societyRepositoryJPA.findById(file.getIdSociety()).orElseThrow(()->new RuntimeException());
		FileEntity newFile = FileMapper.fromDomainToSchema(file);
		newFile.setCreator(societyCreator);
		
		return FileMapper.fromSchemaToDomain(fileRepositoryJPA.save(newFile));
	}



	@Override
	public boolean uploadOnStoreFile(String fileName, Path path, String societyName) {
		try {
			boolean aaaa = storeFile.upload(fileName,path, societyName.toLowerCase());
			log.info(aaaa);
			return aaaa;
		} catch (Exception e) {
			log.info("El archivo no se ha subido: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean existFileById(Long idFile) {
		log.info("Buscando si ya existe el fichero");
		return fileRepositoryJPA.existsById(idFile);
	}
	
	
	
	

}
