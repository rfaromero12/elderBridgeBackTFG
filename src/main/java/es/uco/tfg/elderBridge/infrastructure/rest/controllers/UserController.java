package es.uco.tfg.elderBridge.infrastructure.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uco.tfg.elderBridge.application.usercases.user.ChangePasswordInteractor;
import es.uco.tfg.elderBridge.application.usercases.user.DeleteUserInteractor;
import es.uco.tfg.elderBridge.application.usercases.user.EditProfileInteractor;
import es.uco.tfg.elderBridge.application.usercases.user.LoginUserInteractor;
import es.uco.tfg.elderBridge.application.usercases.user.RecoverPasswordInteractor;
import es.uco.tfg.elderBridge.application.usercases.user.ShowMemberSocietyInteractor;
import es.uco.tfg.elderBridge.application.usercases.user.SignUpUserInteractor;
import es.uco.tfg.elderBridge.infrastructure.exceptions.ErrorDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.CreateUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.DeleteUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.EditUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.LoginUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.ResponseCreateUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.ResponseEditUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.ResponseLoginUserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.user.UserDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/services/user")
public class UserController {
	
	@Autowired
	private DeleteUserInteractor deleteUserInteractor;
	
	@Autowired
	private EditProfileInteractor editUserInteractor;
	
	@Autowired
	private LoginUserInteractor loginUserInteractor;
	@Autowired
	private RecoverPasswordInteractor recoverPasswordInteractor;
	
	@Autowired
	private ShowMemberSocietyInteractor showMemberSocietyInteractor;
	
	@Autowired
	private SignUpUserInteractor signUpUserInteractor;
	
	@Autowired
	private ChangePasswordInteractor changePasswordInteractor; 
	
	@Autowired
	private UserMapper userMapper;
	
	@Operation(summary = "registrar usuario", description = "Endpoint que permite crear un usuario"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario ha sido registrado correctamente"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/auth/signIn")
	public ResponseEntity<ResponseCreateUserDTO> signIn(@Valid @RequestBody CreateUserDTO userDTO){
		ResponseCreateUserDTO response = userMapper.fromUserOutputToResponseCreateUserDTO(signUpUserInteractor.doInteractor(userMapper.fromCreateUserDTOtoUserInput(userDTO)));
		
		return new ResponseEntity<ResponseCreateUserDTO>(response, HttpStatus.OK);
	}

	@Operation(summary = "Iniciar sesion", description = "Endpoint que permite iniciar sesion en el sistema"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario ha iniciado sesion correctamente"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/auth/login")
	public ResponseEntity<ResponseLoginUserDTO> login(@Valid @RequestBody LoginUserDTO userDTO){
		ResponseLoginUserDTO response = userMapper.fromUserOutputToResponseLoginUserDTO(loginUserInteractor.doInteractor(userMapper.fromLoginUserDTOtoUserInput(userDTO)));
		
		return new ResponseEntity<ResponseLoginUserDTO>(response, HttpStatus.OK);
	}
	

	@Operation(summary = "Editar perfil", description = "Endpoint que permite editar el perfil de un usuario"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario ha sido editado correctamente"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/editProfile")
	public ResponseEntity<ResponseEditUserDTO> editProfile(@Valid @RequestBody EditUserDTO userDTO){
		ResponseEditUserDTO response = userMapper.fromUserOutputToResponseEditUserDTO(editUserInteractor.doInteractor(userMapper.fromEditUserDTOtoUserInput(userDTO)));
		
		return new ResponseEntity<ResponseEditUserDTO>(response, HttpStatus.OK);
	}
	

	@Operation(summary = "Eliminar cuenta", description = "Endpoint que permite eliminar la cuenta un usuario"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario ha sido eliminado correctamente"),
	        @ApiResponse(responseCode = "400",  content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/deleteAccount")
	public ResponseEntity<UserDTO> deleteAccount(@Valid @RequestBody DeleteUserDTO userDTO){
		deleteUserInteractor.doInteractor(userMapper.fromDeleteUserDTOtoUserInput(userDTO));
		
		return new ResponseEntity<UserDTO>(HttpStatus.OK);
	}
	

	@Operation(summary = "Recuperar contraseña", description = "Endpoint que permite enviar un correo de recuperación de contraseña al usuario"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El correo ha sido enviado con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/auth/recoverPassword")
	public ResponseEntity<UserDTO> recoverPassword(@Valid @RequestBody UserDTO userDTO){
		recoverPasswordInteractor.doInteractor(userMapper.fromUserDTOtoUserInput(userDTO));
		
		return new ResponseEntity<UserDTO>(HttpStatus.OK);
	}
	

	@Operation(summary = "Cambiar contraseña", description = "Endpoint que permite resetear tu contraseña después de haber solicitado su recuperación"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "La contraseña ha sido cambiada con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/auth/changePasswordRecover")
	public ResponseEntity<UserDTO> changePasswordRecover(@Valid @RequestBody UserDTO userDTO){
		UserDTO response  = userMapper.fromUserOutputToUserDTO(changePasswordInteractor.doInteractor(userMapper.fromUserDTOtoUserInput(userDTO)));
		
		return new ResponseEntity<UserDTO>(response,HttpStatus.OK);
	}
	
	
}
