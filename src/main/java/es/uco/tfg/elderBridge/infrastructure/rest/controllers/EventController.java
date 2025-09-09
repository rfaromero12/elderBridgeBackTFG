package es.uco.tfg.elderBridge.infrastructure.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uco.tfg.elderBridge.application.usercases.event.CreateEventInteractor;
import es.uco.tfg.elderBridge.application.usercases.event.DeleteEventInteractor;
import es.uco.tfg.elderBridge.application.usercases.event.EditEventInteractor;
import es.uco.tfg.elderBridge.application.usercases.event.JoinEventInteractor;
import es.uco.tfg.elderBridge.application.usercases.event.ShowSocietyEventInteractor;
import es.uco.tfg.elderBridge.application.usercases.event.ShowSubsEventInteractor;
import es.uco.tfg.elderBridge.application.usercases.event.UnjoinEventInteractor;
import es.uco.tfg.elderBridge.infrastructure.exceptions.ErrorDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.CreateEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.DeleteEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.EditEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.EventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.JoinEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ResponseCreateEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ResponseEditEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ShowSocietyEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ShowSubsEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.UnjoinEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.mappers.EventMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/services/event")
public class EventController {

	@Autowired
	private CreateEventInteractor createEventInteractor;
	
	@Autowired
	private DeleteEventInteractor deleteEventInteractor;
	
	@Autowired	
	private EditEventInteractor editEventInteractor;
	
	@Autowired
	private JoinEventInteractor joinEventInteractor;
	
	@Autowired
	private UnjoinEventInteractor unJoinEventInteractor;
	
	@Autowired
	private ShowSubsEventInteractor showSubsEventInteractor;
	
	@Autowired
	private ShowSocietyEventInteractor showSocietyEventInteractor;
	
	@Autowired
	private EventMapper eventMapper;

	@Operation(summary = "Crear evento", description = "Endpoint que permite crear un evento"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El evento ha sido registrado correctamente"),
	        @ApiResponse(responseCode = "400",content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/create")
	public ResponseEntity<ResponseCreateEventDTO> create(@Valid @RequestBody CreateEventDTO request){
		ResponseCreateEventDTO response = eventMapper.fromEventOutputToResponseCreateEventDTO(createEventInteractor.doInteractor(eventMapper.fromCreateEventDTOtoEventInput(request)));
		
		return new ResponseEntity<ResponseCreateEventDTO>(response, HttpStatus.OK);
	}
	

	@Operation(summary = "Eliminar evento", description = "Endpoint que permite eliminar un evento"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El evento fue eliminado con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/delete")
	public ResponseEntity<EventDTO> delete(@Valid @RequestBody DeleteEventDTO request){
		deleteEventInteractor.doInteractor(eventMapper.fromDeleteEventDTOtoEventInput(request));
		
		return new ResponseEntity<EventDTO>(HttpStatus.OK);
	}
	

	@Operation(summary = "Editar un evento", description = "Endpoint que permite editar un evento"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El evento fue modificado con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/edit")
	public ResponseEntity<ResponseEditEventDTO> edit(@Valid @RequestBody EditEventDTO request){
		ResponseEditEventDTO response = eventMapper.fromEventOutputToResponseEditEventDTO(editEventInteractor.doInteractor(eventMapper.fromEditEventDTOtoEventInput(request)));
		
		return new ResponseEntity<ResponseEditEventDTO>(response, HttpStatus.OK);
	}
	
	@Operation(summary = "Apuntarse a un evento", description = "Endpoint que permite apuntarse a un evento"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario fue apuntado con exito al evento"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/join")
	public ResponseEntity<EventDTO> joinEvent(@Valid @RequestBody JoinEventDTO request){
		joinEventInteractor.doInteractor(request.userEmail(), request.name());
	
		return new ResponseEntity<EventDTO>(HttpStatus.OK);
	}
	@Operation(summary = "Desapuntarse de un evento", description = "Endpoint que permite desapuntarse de un evento"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El usuario fue desapuntado con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/unjoin")
	public ResponseEntity<EventDTO> unJoinEvent(@Valid @RequestBody UnjoinEventDTO request){
		unJoinEventInteractor.doInteractor(request.userEmail(), request.name());
		
		return new ResponseEntity<EventDTO>(HttpStatus.OK);
	}
	@Operation(summary = "Mostrar eventos suscritos", description = "Endpoint que permite mostrar la lista de evento a los que el usuario está suscrito"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "La consulta ha sido un exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping(path = "/showSubsEvent")
	public ResponseEntity<List<EventDTO>> showSubsEvent(@Valid @RequestBody ShowSubsEventDTO request){
		List<EventDTO> response = eventMapper.fromEventOutputListToEventDTOList(showSubsEventInteractor.doInteractor(request.userEmail()));
		
		return new ResponseEntity<List<EventDTO>>(response,HttpStatus.OK);
	}
	@Operation(summary = "Mostrar eventos de una ONG", description = "Endpoint que permite mostrar los eventos disponibles en una ONG"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "La consulta ha sido un exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})	
	@PostMapping(path = "/showSocietyEvent")
	public ResponseEntity<List<EventDTO>> showSocietyEvent(@Valid @RequestBody ShowSocietyEventDTO request){
		List<EventDTO> response = eventMapper.fromEventOutputListToEventDTOList(showSocietyEventInteractor.doInteractor(request.name()));
		
		return new ResponseEntity<List<EventDTO>>(response,HttpStatus.OK);
	}
	
}
