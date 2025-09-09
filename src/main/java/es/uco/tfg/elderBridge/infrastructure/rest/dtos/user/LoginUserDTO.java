package es.uco.tfg.elderBridge.infrastructure.rest.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDTO(	
		@NotBlank(message = "El email es un campo obligatorio") 
		@Email(message = "El campo email no tiene formato de correo electronico") String email,
		@NotBlank(message="El campo contraseña es un campo obligatorio") String password
		){

}
