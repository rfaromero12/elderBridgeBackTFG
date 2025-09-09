package es.uco.tfg.elderBridge.application.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.application.out.FileOutput;
import es.uco.tfg.elderBridge.domain.models.File;




@Component("applicationFileMapper")
public class FileMapper {
	public static FileOutput toOut(File file) {
		if (file == null) {
			return null;
		}
		return FileOutput.builder()
				.url(file.getUrl())
				.description(file.getDescription())
				.idFile(file.getIdFile())
				.idSociety(file.getIdSociety())
				.name(file.getName())
				.fileContent(file.getFileContent())
				.build();
	}
	
	public static File fromInput(FileInput fileInput) {
		if (fileInput == null) {
			return null;
		}
		return File.builder()
				.url(fileInput.getUrl())
				.description(fileInput.getDescription())
				.idSociety(fileInput.getIdSociety())
				.name(fileInput.getName())
				.idFile(fileInput.getIdFile())
				.build();
	}

	public static List<FileOutput> toOutList(List<File> listFiles) {
		if (Objects.isNull(listFiles) || listFiles.isEmpty()) {
			return Collections.emptyList();
		}
		List<FileOutput> fileOutputs = new ArrayList<>();
		
		listFiles.forEach(file -> fileOutputs.add(toOut(file)));
		
		return fileOutputs;
	}
}