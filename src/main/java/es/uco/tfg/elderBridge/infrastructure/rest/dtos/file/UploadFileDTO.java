package es.uco.tfg.elderBridge.infrastructure.rest.dtos.file;

import java.nio.file.Path;

public record UploadFileDTO(
		String name,
		Path filePath ,
		Long idSociety
		) {

}
