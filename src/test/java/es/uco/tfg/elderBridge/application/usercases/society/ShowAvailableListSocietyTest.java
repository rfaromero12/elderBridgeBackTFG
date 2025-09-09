package es.uco.tfg.elderBridge.application.usercases.society;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.SocietyAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.MemberShipRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.CreateSocietyDTO;

@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo caso de uso Show available list society")
public class ShowAvailableListSocietyTest {
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	@Mock
	private UserRepositoryJPA userRepositoryJPA;
	
	@Mock
	private MemberShipRepositoryJPA memberShipRepositoryJPA;
	
	private ShowAvailableSocietyInteractor showAvailableSocietyInteractor ;
	
	private SocietyInput societyInput;
	
	private List<SocietyEntity> societyEntity;
	
	private SocietyAdapter societyAdapter;
	
	private List<SocietyEntity> subsEntities;
	
	private UserEntity userEntity;
	
	@BeforeEach
	void setUp() {
		userEntity = UserEntity.builder()
	             .userId(1L)
	             .email("nuevo@example.com")
	             .password("hashedPassword123")
	             .name("Nuevo")
	             .surname("Usuario")
	             .build();
		societyEntity =	List.of(SocietyEntity.builder()
				.creator(userEntity)
				.email("ejemplo@gmail.com")
				.societyId(null)
				.location("locationEjemplo")
				.name("nombreEjemplo")
				.memberships(null)
				.description("description").build(),
				SocietyEntity.builder()
				.creator(userEntity)
				.email("ejemplo@gmail.com")
				.societyId(null)
				.location("locationEjemplo2")
				.name("nombreEjemplo2")
				.memberships(null)
				.description("description").build());	
		
		subsEntities = List.of(SocietyEntity.builder()
				.creator(userEntity)
				.email("ejemplo@gmail.com")
				.societyId(null)
				.location("locationEjemplo")
				.name("nombreEjemplo")
				.memberships(null)
				.description("description").build());
		societyAdapter = new SocietyAdapter(societyRepositoryJPA, userRepositoryJPA, memberShipRepositoryJPA);
		societyInput = SocietyInput.builder()
				.idUserCreator(1L)
				.email("ejemplo@gmail.com")
				.idSociety(null)
				.location("locationEjemplo")
				.name("nombreEjemplo")
				.description("description").build();	
		showAvailableSocietyInteractor = new ShowAvailableSocietyInteractor(societyAdapter);
	
	}
	
	
	@Test
	@DisplayName("HappyCase")
	void shouldReturnNSocieties() {
		when(memberShipRepositoryJPA.findSocietyByUserEmail("userEmail@gmail.com")).thenReturn(subsEntities);
		when(societyRepositoryJPA.findAll()).thenReturn(societyEntity);
		List<SocietyOutput> result = showAvailableSocietyInteractor.doInteractor("userEmail@gmail.com");
	
		assertEquals(1, result.size());
		assertEquals("nombreEjemplo2", result.get(0).getName());
		
		
	}
	
	@Test
	@DisplayName("Lista vacia")
	void should_return_empty_list() {
		when(memberShipRepositoryJPA.findSocietyByUserEmail("userEmail@gmail.com")).thenReturn(societyEntity);
		when(societyRepositoryJPA.findAll()).thenReturn(societyEntity);
		List<SocietyOutput> result = showAvailableSocietyInteractor.doInteractor("userEmail@gmail.com");
		assertEquals(0, result.size());

	}
	
}
