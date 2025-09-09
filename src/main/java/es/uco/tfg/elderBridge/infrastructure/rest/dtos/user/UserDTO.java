package es.uco.tfg.elderBridge.infrastructure.rest.dtos.user;

public record UserDTO(Long userId, String name,String surname,
		String email,String password,  String confirmPassword,
		String codeNumber, String rol){

}
