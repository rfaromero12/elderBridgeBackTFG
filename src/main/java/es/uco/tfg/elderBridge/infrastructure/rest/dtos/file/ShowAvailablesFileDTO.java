package es.uco.tfg.elderBridge.infrastructure.rest.dtos.file;

import jakarta.validation.constraints.NotBlank;

public record ShowAvailablesFileDTO(
		@NotBlank String idSociety
		) {

}
