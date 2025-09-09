package es.uco.tfg.elderBridge.infrastructure.database.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.uco.tfg.elderBridge.domain.models.Event;
import es.uco.tfg.elderBridge.domain.models.User;
import es.uco.tfg.elderBridge.domain.ports.EventPort;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers.EventMapper;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers.UserMapper;
import es.uco.tfg.elderBridge.infrastructure.database.entity.EventEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.ParticipantEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.EventEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.ParticipantRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.mail.MailService;
import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class EventAdapter implements EventPort{

	private EventEntityRepositoryJPA eventEntityRepositoryJPA;
	private ParticipantRepositoryJPA participantRepositoryJPA;
	private UserRepositoryJPA userRepositoryJPA;
	private SocietyRepositoryJPA societyRepositoryJPA;
	private MailService mailService;
	
	public EventAdapter(EventEntityRepositoryJPA repository,
			ParticipantRepositoryJPA participantRepositoryJPA,
			UserRepositoryJPA userRepositoryJPA,
			MailService mailservice,
			SocietyRepositoryJPA societyRepositoryJPA) {
		
		this.eventEntityRepositoryJPA = repository;
		this.participantRepositoryJPA = participantRepositoryJPA;
		this.userRepositoryJPA = userRepositoryJPA;
		this.societyRepositoryJPA = societyRepositoryJPA;
		this.mailService = mailservice;
	}
	
	@Transactional
	@Override
	public Event createEvent(Event fromInput) {
			
			UserEntity userEntity = userRepositoryJPA.findById(fromInput.getCreatorId()).get();
			
			SocietyEntity societyEntity = societyRepositoryJPA.findById(fromInput.getSocietyOrganizerId()).get();
			EventEntity eventEntity = EventMapper.fromDomainToSchema(fromInput);
			eventEntity.setUserCreator(userEntity);
			eventEntity.setSocietyOrganizer(societyEntity);
			societyEntity.getAvailablesEvent().add(eventEntity);
			ParticipantEntity participantEntity= ParticipantEntity.builder()
			.member(userEntity)
			.event(eventEntity)
			.build();
			eventEntity.getParticipantEntities().add(participantEntity);
			
			societyRepositoryJPA.save(societyEntity);
			
			return EventMapper.fromSchemaToDomain(eventEntity, societyEntity.getSocietyId());

	}
	
	@Transactional
	@Override
	public void deleteEvent(Event fromInput, SimpleMailMessage message) {
		EventEntity eventDelete = eventEntityRepositoryJPA.findEventByName(fromInput.getName()).
				orElseThrow(()->new RuntimeException());
		List<ParticipantEntity> participantsList = eventDelete.getParticipantEntities();
		eventDelete.getSocietyOrganizer().getAvailablesEvent().remove(eventDelete);
		eventDelete.getUserCreator().getEventCreatedBy().remove(eventDelete);
		eventEntityRepositoryJPA.delete(eventDelete);
		if (participantsList.size() > 0 ) {
			List<String> participantsEmail = participantsList.stream().map(participants->participants.getMember().getEmail()).toList();
			mailService.sendMultiplesEmail(participantsEmail, message);
		}
		log.info("El evento " + fromInput.getName() + " ha sido eliminado con exito");
	
		
	}
	
	@Transactional
	@Override
	public void joinEvent(String userEmail, String eventName) {
		UserEntity userEntity = userRepositoryJPA.findUserByEmail(userEmail).get();
		
		EventEntity eventEntity = eventEntityRepositoryJPA.findEventByName(eventName).get();
		
		ParticipantEntity participantEntity= ParticipantEntity.builder()
				.member(userEntity)
				.event(eventEntity)
				.build();
		eventEntity.getParticipantEntities().add(participantEntity);
		eventEntityRepositoryJPA.save(eventEntity);
		log.info("El usuario ya participa en el evento");
		
	}

	@Override
	public boolean unJoinEvent(String userEmail, String eventName) {
		EventEntity eventEntity = eventEntityRepositoryJPA.findEventByName(eventName).get();
		
		boolean participantRemove = eventEntity.getParticipantEntities().removeIf(eventParticipant -> 
					eventParticipant.getEvent().getName().equals(eventName) && eventParticipant.getMember().getEmail().equals(userEmail));
		
		
		if (!participantRemove) {
			return false;
		}
		eventEntityRepositoryJPA.save(eventEntity);
		return true;
	}

	@Override
	public List<Event> showSubsEvent(String userEmail) {
		List<EventEntity> eventsList = participantRepositoryJPA.findEventsByUserEmail(userEmail);
		if (Objects.isNull(eventsList)) {
			return Collections.emptyList();
		}
		log.info("Se han encontrado " +  eventsList.size());
		
		List<Event> eventToReturn = new ArrayList<>();
		eventsList.forEach(event->{
			eventToReturn.add(EventMapper.fromSchemaToDomain(event));
		});
		return eventToReturn;
	}

	@Override
	public List<Event> showSocietyEvent(String name) {
		List<EventEntity> eventsList = eventEntityRepositoryJPA.findEventsByName(name);
		if (Objects.isNull(eventsList)) {
			return Collections.emptyList();
		}
		log.info("Se han encontrado " +  eventsList.size());
			
			List<Event> eventToReturn = new ArrayList<>();
		eventsList.forEach(event->{
			eventToReturn.add(EventMapper.fromSchemaToDomain(event));
		});
		return eventToReturn;
	}

	@Override
	public Event findEventByName(String name) {
		EventEntity eventEntity = eventEntityRepositoryJPA.findEventByName(name).orElseGet(()-> null);
		return EventMapper.fromSchemaToDomain(eventEntity);
	}

	@Override
	public Event findById(Long eventId) {
		EventEntity eventEntity = eventEntityRepositoryJPA.findById(eventId).orElseGet(()-> null);
		return EventMapper.fromSchemaToDomain(eventEntity);
	}

	@Override
	public void sendChangeEventDateEmail(Long eventId, SimpleMailMessage message) {
		List<UserEntity> participantsList = participantRepositoryJPA.findUserByEventId(eventId);
		if (participantsList.size() > 0 ) {
			List<String> participantsEmail = participantsList.stream().map(user->user.getEmail()).toList();
			mailService.sendMultiplesEmail(participantsEmail, message);
		}
		
	}

	@Override
	public void update(Event event) {
		EventEntity eventEntity = eventEntityRepositoryJPA.findById(event.getEventId()).get();
		eventEntity.setDescription(event.getDescription());
		eventEntity.setEventDate(event.getEventDate());
		eventEntity.setEventLocation(event.getEventLocation());		
		
		eventEntityRepositoryJPA.save(eventEntity);
	}

	@Override
	public List<User> getParticipantList(String eventName) {
		EventEntity eventEntity = eventEntityRepositoryJPA.findEventByName(eventName).orElseGet(()-> null);
		List<User> participants = new ArrayList<>();

		if (eventEntity != null) {
			eventEntity.getParticipantEntities().forEach(event -> participants.add(UserMapper.fromSchemaToDomain(event.getMember())));
		}
		
		return participants;
	}
	
	

}
