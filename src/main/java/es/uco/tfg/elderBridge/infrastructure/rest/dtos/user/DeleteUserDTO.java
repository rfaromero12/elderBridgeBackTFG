package es.uco.tfg.elderBridge.infrastructure.rest.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeleteUserDTO(
		Long userId,
		@NotBlank(message = "El apellido es un campo obligatorio") @Email(message = "El campo email no tiene formato de correo electronico") String email
		){

}
