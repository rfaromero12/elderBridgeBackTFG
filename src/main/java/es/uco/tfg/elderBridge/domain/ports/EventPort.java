package es.uco.tfg.elderBridge.domain.ports;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;

import es.uco.tfg.elderBridge.domain.models.Event;
import es.uco.tfg.elderBridge.domain.models.User;

public interface EventPort {

	Event createEvent(Event fromInput);

	void deleteEvent(Event fromInput, SimpleMailMessage message);

	void joinEvent(String userEmail, String eventName);

	boolean unJoinEvent(String userEmail, String eventName);

	List<Event> showSubsEvent(String userEmail);

	List<Event> showSocietyEvent(String name);

	Event findEventByName(String name);

	Event findById(Long eventId);

	void sendChangeEventDateEmail(Long eventId, SimpleMailMessage message);

	void update(Event event);

	List<User> getParticipantList(String eventName);

}
