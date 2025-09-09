package es.uco.tfg.elderBridge.domain.ports;

import java.nio.file.Path;
import java.util.List;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.domain.models.File;

public interface FilePort {
	
	public List<File> getFiles(File file);
	
	public File download(File file);

	public File findFileByName(String name);

	public boolean existFileByNameAndSocietyId(String name, Long idSociety);

	public void delete(File savedFile);

	public File save(File fileInput);

	public boolean uploadOnStoreFile(String fileName, Path path, String societyName);

	public boolean existFileById(Long idFile);
}