package es.uco.tfg.elderBridge.infrastructure.database.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.domain.ports.SocietyPort;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers.SocietyMapper;
import es.uco.tfg.elderBridge.infrastructure.database.entity.MembershipEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.MemberShipRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SocietyAdapter implements SocietyPort{
	
	private SocietyRepositoryJPA societyRepositoryJPA;
	private UserRepositoryJPA userRepositoryJPA;
	private MemberShipRepositoryJPA memberShipRepositoryJPA;
	
	public SocietyAdapter(SocietyRepositoryJPA societyRepository,UserRepositoryJPA userRepository, MemberShipRepositoryJPA memberShipRepository) {
		this.societyRepositoryJPA = societyRepository;
		this.userRepositoryJPA = userRepository;
		this.memberShipRepositoryJPA = memberShipRepository;
	}
	
	@Transactional
	@Override
	public Society createSociety(Society fromInput) {
			UserEntity societyOwner = userRepositoryJPA.findById(fromInput.getIdUserCreator()).get();
			SocietyEntity newSociety=  SocietyMapper.fromDomainToSchema(fromInput,societyOwner);
			societyOwner.setCreatedOrganization(newSociety);

			MembershipEntity membershipEntity = MembershipEntity.builder().member(societyOwner).society(newSociety).build();
			newSociety.getMemberships().add(membershipEntity);
			userRepositoryJPA.save(societyOwner);
			return SocietyMapper.fromSchemaToDomain(newSociety);
	}
	@Transactional
	@Override
	public Society update(Society society) {
			SocietyEntity societyEntity = societyRepositoryJPA.findById(society.getIdSociety()).get();
			societyEntity.setName(society.getName());
			societyEntity.setLocation(society.getLocation());
			societyEntity.setDescription(society.getDescription());
			return SocietyMapper.fromSchemaToDomain(societyRepositoryJPA.save(societyEntity));		
	}

	@Transactional
	@Override
	public void deleteSociety(Society fromInput) {
			SocietyEntity entity = societyRepositoryJPA.findById(fromInput.getIdSociety()).orElseThrow(()->new RuntimeException());
			entity.getCreator().setCreatedOrganization(null);
			societyRepositoryJPA.delete(entity);
			log.info("La ONG " + fromInput.getIdSociety() + " ha sido eliminada con exito");		
	}


	@Transactional
	@Override
	public void joinSociety(String userEmail, String societyName) {

		UserEntity userEntity = userRepositoryJPA.findUserByEmail(userEmail).get();
		
		SocietyEntity societyEntity = societyRepositoryJPA.findSocietyByName(societyName).get();
		MembershipEntity membershipEntity = MembershipEntity.builder()
				.member(userEntity)
				.society(societyEntity)
				.build();
		
		memberShipRepositoryJPA.save(membershipEntity);
		log.info("El usuario es miembro");
	}

	@Transactional
	@Override
	public void unjoinSociety(String userEmail, String societyName) {
		
		memberShipRepositoryJPA.unJoin(userEmail, societyName);
		log.info("El usuario ya no es miembro");
		
	}

	@Override
	public List<Society> showAvailableSocieties(String idUserCreator) {
		List<SocietyEntity> societyAvailableList = memberShipRepositoryJPA.findSocietyByUserEmail(idUserCreator);
		if (Objects.isNull(societyAvailableList)) {
			return Collections.emptyList();
		}
		
		List<SocietyEntity> societyList = societyRepositoryJPA.findAll();
		
		log.info("Se han encontrado " +  societyList.size());
		log.info("El usuario esta subscrito a " + societyAvailableList.size());
		
		if (societyList.size() == societyAvailableList.size()) {
 			return Collections.emptyList();
		}
		
		List<Society> societyToReturn = new ArrayList<>();
		societyList.stream().
		filter(society -> !societyAvailableList.contains(society))
		.forEach(society->{
			societyToReturn.add(SocietyMapper.fromSchemaToDomain(society));});
		
		
		
		return societyToReturn;
	}
	
	@Override
	public List<Society> showSubSociety(String userEmail) {
		
		List<SocietyEntity> societyAvailableList = memberShipRepositoryJPA.findSocietyByUserEmail(userEmail);
		if (Objects.isNull(societyAvailableList)) {
			return Collections.emptyList();
		}
		log.info("Se han encontrado " +  societyAvailableList.size());
		
		List<Society> societyToReturn = new ArrayList<>();
		societyAvailableList.forEach(society->{
			societyToReturn.add(SocietyMapper.fromSchemaToDomain(society));
		});
		
		return societyToReturn;
	}

	@Override
	public Society findById(Long idSociety) {
		Optional<SocietyEntity> societyEntity = societyRepositoryJPA.findById(idSociety);
		return SocietyMapper.fromSchemaToDomain(societyEntity.orElseGet(()->null));
	}

	@Override
	public Society findSocietyByName(String name) {
		Optional<SocietyEntity> societyEntity = societyRepositoryJPA.findSocietyByName(name);
		return SocietyMapper.fromSchemaToDomain(societyEntity.orElseGet(()->null));
	}

	@Override
	public Society findSocietyByEmail(String email) {
		Optional<SocietyEntity> societyEntity = societyRepositoryJPA.findSocietyByEmail(email);
		return SocietyMapper.fromSchemaToDomain(societyEntity.orElseGet(()->null));
	}

	@Override
	public Society findSocietyByIdUserCreator(Long idUserCreator) {
		Optional<SocietyEntity> societyEntity = societyRepositoryJPA.findSocietyByIdUserCreator(idUserCreator);
		return SocietyMapper.fromSchemaToDomain(societyEntity.orElseGet(()->null));
	}

}
