package es.uco.tfg.elderBridge.infrastructure.rest.dtos.society;

import jakarta.validation.constraints.NotBlank;

public record UnJoinSocietyDTO(
		@NotBlank(message ="El email es un campo obligatorio") String email,
		@NotBlank(message ="El nombre es un campo obligatorio")String name) {

}
