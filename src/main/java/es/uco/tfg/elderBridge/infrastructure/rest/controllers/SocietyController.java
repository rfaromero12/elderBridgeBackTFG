package es.uco.tfg.elderBridge.infrastructure.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uco.tfg.elderBridge.application.usercases.society.CreateSocietyInteractor;
import es.uco.tfg.elderBridge.application.usercases.society.DeleteSocietyInteractor;
import es.uco.tfg.elderBridge.application.usercases.society.EditSocietyInteractor;
import es.uco.tfg.elderBridge.application.usercases.society.JoinSocietyInteractor;
import es.uco.tfg.elderBridge.application.usercases.society.ShowAvailableSocietyInteractor;
import es.uco.tfg.elderBridge.application.usercases.society.ShowListSocietyInteractor;
import es.uco.tfg.elderBridge.application.usercases.society.UnjoinSocietyInteractor;
import es.uco.tfg.elderBridge.infrastructure.exceptions.ErrorDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ResponseCreateEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.CreateSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.DeleteSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.EditSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.JoinSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.ResponseEditSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.SocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.UnJoinSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.mappers.SocietyMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/services/society")
public class SocietyController {

	@Autowired
	private CreateSocietyInteractor createSocietyInteractor;
	
	@Autowired
	private DeleteSocietyInteractor deleteSocietyInteractor;
	
	@Autowired
	private EditSocietyInteractor editSocietyInteractor;
	
	@Autowired
	private JoinSocietyInteractor joinSocietyInteractor;
	
	@Autowired
	private UnjoinSocietyInteractor unJoinSocietyInteractor;
	
	@Autowired
	private ShowListSocietyInteractor showListSocietyInteractor;
	
	@Autowired 
	private ShowAvailableSocietyInteractor showAvailableSocietyInteractor;
	
	@Autowired
	private SocietyMapper societyMapper;
	
	
	@Operation(summary = "Crear una ONG", description = "Endpoint que permite crear una ONG"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "La ONG fue creada con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/create")
	public ResponseEntity<ResponseCreateEventDTO> create(@Valid @RequestBody CreateSocietyDTO request){
		ResponseCreateEventDTO response = societyMapper.fromSocietyOutputToResponseCreateSocietyDTO(createSocietyInteractor.doInteractor(societyMapper.fromCreateSocietyDTOtoSocietyInput(request)));
		
		return new ResponseEntity<ResponseCreateEventDTO>(response, HttpStatus.OK);
	}
	
	@Operation(summary = "Eliminar ONG", description = "Endpoint que permite borrar una ONG"	)
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "La ONG fue borrada con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/delete")
	public ResponseEntity<SocietyDTO> delete(@Valid @RequestBody DeleteSocietyDTO request){
		deleteSocietyInteractor.doInteractor(societyMapper.fromDeleteSocietyDTOtoSocietyInput(request));
		
		return new ResponseEntity<SocietyDTO>(HttpStatus.OK);
	}
	@Operation(summary = "Editar una ONG", description = "Endpoint que permite editar una ONG"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El evento fue modificado con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/edit")
	public ResponseEntity<ResponseEditSocietyDTO> edit(@Valid @RequestBody EditSocietyDTO request){
		ResponseEditSocietyDTO response = societyMapper.fromSocietyOutputToResponseEditSocietyDTO(editSocietyInteractor.doInteractor(societyMapper.fromEditSocietyDTOtoSocietyInput(request)));
		
		return new ResponseEntity<ResponseEditSocietyDTO>(response, HttpStatus.OK);
	}
	@Operation(summary = "Hacerse miembro de una ONG", description = "Endpoint que permite hacerse miembro de una ONG"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario se ha hecho miembro con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/join")
	public ResponseEntity<SocietyDTO> join(@Valid @RequestBody JoinSocietyDTO societyDTO){
		joinSocietyInteractor.doInteractor(societyDTO.email(), societyDTO.name());
		
		return new ResponseEntity<SocietyDTO>(HttpStatus.OK);
	}
	@Operation(summary = "Mostrar ONGs suscritas", description = "Endpoint que permite mostrar las ONGs de las que un usuario es miembro"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "La consulta ha sido un exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/showSubList")
	public ResponseEntity<List<SocietyDTO>> showSubList(@RequestBody SocietyDTO societyDTO){
		List<SocietyDTO> response = societyMapper.fromSocietyOutputToSocietyDTO(showListSocietyInteractor.doInteractor(societyDTO.getEmail()));
		
		return new ResponseEntity<List<SocietyDTO>>(response, HttpStatus.OK);
	}
	
	@Operation(summary = "Dejar de ser miembro de una ONG", description = "Endpoint que permite dejar de ser miembro de una ONG"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario ha dejado de ser miembro con exito"),
	        @ApiResponse(responseCode = "400",content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/unjoin")
	public ResponseEntity<SocietyDTO> unJoin(@Valid @RequestBody UnJoinSocietyDTO societyDTO){
		unJoinSocietyInteractor.doInteractor(societyDTO.email(), societyDTO.name());
		
		return new ResponseEntity<SocietyDTO>(HttpStatus.OK);
	}
	
	@Operation(summary = "Mostrar ONGs disponibles", description = "Endpoint que permite mostrar las ONGs disponibles en el sistema"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El evento fue modificado con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/showAvailableList")
	public ResponseEntity<List<SocietyDTO>> showAvalaibleSocieties(@RequestBody SocietyDTO societyDTO){
		List<SocietyDTO> response = societyMapper.fromSocietyOutputToSocietyDTO(showAvailableSocietyInteractor.doInteractor(societyDTO.getEmail()));
		
		return new ResponseEntity<List<SocietyDTO>>(response, HttpStatus.OK);
	}
	
}
