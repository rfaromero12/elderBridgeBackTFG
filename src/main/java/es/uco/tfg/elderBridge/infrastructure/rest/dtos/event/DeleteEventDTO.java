package es.uco.tfg.elderBridge.infrastructure.rest.dtos.event;

import jakarta.validation.constraints.NotBlank;

public record DeleteEventDTO(
		@NotBlank(message = "El nombre del evento es obligatorio") String name
		) {

}
