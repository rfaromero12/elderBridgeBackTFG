package es.uco.tfg.elderBridge.infrastructure.rest.dtos.society;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EditSocietyDTO(
		@NotBlank(message = "El nombre de la ONG es obligatorio") String name,
		@NotBlank(message = "El nombre de la ONG es obligatorio") String location,
		Long idSociety,
		@NotBlank(message = "El nombre de la ONG es obligatorio") @Email(message = "No tiene formato email") String email,
		Long idUserCreator,
		@NotBlank(message = "El nombre de la ONG es obligatorio") String description
		) {

}
