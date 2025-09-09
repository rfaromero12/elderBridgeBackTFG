package es.uco.tfg.elderBridge.infrastructure.rest.dtos.event;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public record EditEventDTO(
		@NotBlank(message = "El nombre del evento es obligatorio") String name,
		LocalDateTime eventDate,	
		@NotBlank(message = "El localizacion del evento es un campo obligatorio") String eventLocation,
		@NotBlank(message = "La descripcion del evento es obligatoria") String description,
		Long eventId
		) {

}
