package es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.models.File;
import es.uco.tfg.elderBridge.infrastructure.database.entity.FileEntity;

@Component("adapterFileMapper")
public class FileMapper {
	public static FileEntity fromDomainToSchema(File file) {
		if (file == null) {
			return null;
		}
		return FileEntity.builder()
				.description(file.getDescription())
				.fileId(file.getIdFile())
				.name(file.getName())
				.build();
	}
	
	public static File fromSchemaToDomain(FileEntity fileInput) {
		if (fileInput == null) {
			return null;
		}
		return File.builder()
				.description(fileInput.getDescription())
				.idSociety(fileInput.getCreator().getSocietyId())
				.idFile(fileInput.getFileId())
				.name(fileInput.getName())
				.build();
	}

	public static List<File> toOutList(List<FileEntity> listFiles) {
		if (Objects.isNull(listFiles) || listFiles.isEmpty()) {
			return Collections.emptyList();
		}
		List<File> fileOutputs = new ArrayList<>();
		
		listFiles.forEach(file -> fileOutputs.add(fromSchemaToDomain(file)));
		
		return fileOutputs;
	}
}
