 package es.uco.tfg.elderBridge.infrastructure.rest.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.CreateEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.DeleteEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.EditEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.EventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.JoinEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ResponseCreateEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ResponseEditEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.UnjoinEventDTO;

@Mapper(componentModel = "spring")
public interface EventMapper {
	EventInput fromCreateEventDTOtoEventInput(CreateEventDTO createEvent);
	EventInput fromDeleteEventDTOtoEventInput(DeleteEventDTO deleteEvent);
	EventInput fromEditEventDTOtoEventInput(EditEventDTO editEvent);
	EventInput fromJoinEventDTOtoEventInput(JoinEventDTO joinEvent);
	EventInput fromUnjoinEventDTOtoEventInput(UnjoinEventDTO unjoinEvent);


	
	ResponseCreateEventDTO fromEventOutputToResponseCreateEventDTO(EventOutput eventOutput);
	ResponseEditEventDTO fromEventOutputToResponseEditEventDTO(EventOutput eventOutput);

	
	List<EventDTO> fromEventOutputListToEventDTOList(List<EventOutput> listEvent);

}
