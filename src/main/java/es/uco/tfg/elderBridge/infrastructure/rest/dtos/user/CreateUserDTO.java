package es.uco.tfg.elderBridge.infrastructure.rest.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(@NotBlank(message = "El nombre es un campo obligatorio") String name,
		@NotBlank(message = "El apellido es un campo obligatorio") String surname,
		@NotBlank(message = "El apellido es un campo obligatorio") @Email(message = "El campo email no tiene formato de correo electronico") String email,
		@NotBlank(message = "La contraseña es un campo obligatorio") String password,  
		@NotBlank(message = "La confirmación de la contraseña es un campo obligatorio") String confirmPassword,
		String rol) {

}
