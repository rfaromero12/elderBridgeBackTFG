package es.uco.tfg.elderBridge.infrastructure.rest.dtos.society;

import jakarta.validation.constraints.NotNull;

public record DeleteSocietyDTO(
		@NotNull Long idSociety
		) {

}
