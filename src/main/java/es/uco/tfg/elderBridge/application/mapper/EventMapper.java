package es.uco.tfg.elderBridge.application.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.EventInput;
import es.uco.tfg.elderBridge.application.out.EventOutput;
import es.uco.tfg.elderBridge.domain.models.Event;

@Component("applicationEventMapper")
public class EventMapper {
	public static EventOutput toOut(Event event) {
		if (event == null) {
			return null;
		}
		return EventOutput.builder()
				.eventId(event.getEventId())
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.eventLocation(event.getEventLocation())
				.name(event.getName())
				.creatorId(event.getCreatorId())
				.societyOrganizerId(event.getSocietyOrganizerId())
				.build();
	}
	
	public static Event fromInput(EventInput eventInput) {
		if (eventInput == null) {
			return null;
		}
		return Event.builder()
				.eventId(eventInput.getEventId())
				.description(eventInput.getDescription())
				.eventDate(eventInput.getEventDate())
				.eventLocation(eventInput.getEventLocation())
				.name(eventInput.getName())
				.creatorId(eventInput.getUserId())
				.societyOrganizerId(eventInput.getSocietyId())
				.build();
	}

	public static List<EventOutput> toOutList(List<Event> showSubsEvent) {
		if (Objects.isNull(showSubsEvent) || showSubsEvent.isEmpty()) {
			return Collections.emptyList();
		}
		List<EventOutput> eventOutputs = new ArrayList<>();
		
		showSubsEvent.forEach(event -> eventOutputs.add(toOut(event)));
		
		return eventOutputs;
	}
}
