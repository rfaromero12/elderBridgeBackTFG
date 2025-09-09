package es.uco.tfg.elderBridge.infrastructure.rest.dtos.file;

import java.util.List;

public record ResponseShowAvailablesFileDTO(
			List<FileDTO> listFiles
		) {

}
