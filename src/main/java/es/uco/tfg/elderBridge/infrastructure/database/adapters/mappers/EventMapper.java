package es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.models.Event;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;

@Component("adapterEventMapper")
public class EventMapper {

	public static EventEntity fromDomainToSchema(Event fromInput) {
		if (fromInput == null) {
			return null;
		}
		return EventEntity.builder()
				.eventId(fromInput.getEventId())
				.description(fromInput.getDescription())
				.eventDate(fromInput.getEventDate())
				.eventLocation(fromInput.getEventLocation())
				.name(fromInput.getName())
				.build();	
		}

	public static Event fromSchemaToDomain(EventEntity eventEntity) {
		if (eventEntity == null) {
			return null;
		}
		return Event.builder()
				.eventId(eventEntity.getEventId())
				.description(eventEntity.getDescription())
				.eventDate(eventEntity.getEventDate())
				.eventLocation(eventEntity.getEventLocation())
				.name(eventEntity.getName())
				.creatorId(eventEntity.getUserCreator().getUserId())
				.societyOrganizerId(eventEntity.getSocietyOrganizer().getSocietyId())
				.build();
	}

	public static Event fromSchemaToDomain(EventEntity eventEntity, Long idSociety) {
		if (eventEntity == null) {
			return null;
		}
		return Event.builder()
				.eventId(eventEntity.getEventId())
				.description(eventEntity.getDescription())
				.eventDate(eventEntity.getEventDate())
				.eventLocation(eventEntity.getEventLocation())
				.name(eventEntity.getName())
				.creatorId(eventEntity.getUserCreator().getUserId())
				.societyOrganizerId(idSociety)
				.build();
	}

}
