package es.uco.tfg.elderBridge.infrastructure.rest.dtos.event;

import jakarta.validation.constraints.NotBlank;

public record ShowSubsEventDTO(
		
		@NotBlank(message = "El email del usuario es obligatorio") String userEmail){

}
