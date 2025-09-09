package es.uco.tfg.elderBridge.infrastructure.rest.mappers;


import org.mapstruct.Mapper;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.out.UserOutput;
import es.uco.tfg.elderBridge.infrastructure.rest.controllers.UserController;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.CreateUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.DeleteUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.EditUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.LoginUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.ResponseCreateUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.ResponseEditUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.ResponseLoginUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.UserDTO;


@Mapper(componentModel="spring", uses = UserController.class)
public interface UserMapper {
	UserInput fromUserDTOtoUserInput(UserDTO userDTO);
	UserDTO fromUserOutputToUserDTO(UserOutput userOutput);
	
	//Request de entrada
	UserInput fromDeleteUserDTOtoUserInput (DeleteUserDTO deleteUserDTO);
	UserInput fromCreateUserDTOtoUserInput (CreateUserDTO createUserDTO);
	UserInput fromEditUserDTOtoUserInput (EditUserDTO editUserDTO);
	UserInput fromFindUserDTOtoUserInput (CreateUserDTO createUserDTO);
	UserInput fromLoginUserDTOtoUserInput(LoginUserDTO loginUserDTO);

	//Response salida
	ResponseCreateUserDTO fromUserOutputToResponseCreateUserDTO(UserOutput userOutput);
	ResponseEditUserDTO fromUserOutputToResponseEditUserDTO(UserOutput userOutput);
	ResponseLoginUserDTO fromUserOutputToResponseLoginUserDTO(UserOutput userOutput);

	
//	public UserInput fromUserDTOtoUserInput(UserDTO userDTO) {
//		if(Objects.isNull(userDTO))
//			return null;
//		
//		return UserInput.builder()
//				.email(userDTO.getEmail())
//				.name(userDTO.getName())
//				.password(userDTO.getPassword())
//				.confirmPassword(userDTO.getConfirmPassword())
//				.codeNumber(userDTO.getCodeNumber())
//				.rol(userDTO.getRol())
//				.surname(userDTO.getSurname())
//				.userId(userDTO.getUserId())
//				.build();
//	}
//
//	public UserDTO fromUserOutputToUserDTO(UserOutput userOutput) {
//		if(Objects.isNull(userOutput))
//			return null;
//		
//		return UserDTO.builder()
//				.email(userOutput.getEmail())
//				.name(userOutput.getName())
//				.password(userOutput.getPassword())
//				.rol(userOutput.getRol())
//				.surname(userOutput.getSurname())
//				.userId(userOutput.getUserId())
//				.build();
//	}

}
