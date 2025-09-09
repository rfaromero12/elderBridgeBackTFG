package es.uco.tfg.elderBridge.infrastructure.rest.dtos.event;

import jakarta.validation.constraints.NotBlank;

public record UnjoinEventDTO(
		@NotBlank(message = "El nombre del evento es obligatorio") String name,
		@NotBlank(message = "El email del usuario es obligatirio") String userEmail
		) {

}
