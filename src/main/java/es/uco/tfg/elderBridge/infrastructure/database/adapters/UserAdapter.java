package es.uco.tfg.elderBridge.infrastructure.database.adapters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.domain.models.User;
import es.uco.tfg.elderBridge.domain.ports.UserPort;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers.SocietyMapper;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers.UserMapper;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.MemberShipRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;
import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class UserAdapter implements UserPort{
	
	private UserRepositoryJPA userRepositoryJPA;
	private MemberShipRepositoryJPA memberShipRepositoryJPA;

	
	
	public UserAdapter(UserRepositoryJPA repository, MemberShipRepositoryJPA memberShipRepoJpa) {
		this.userRepositoryJPA = repository;
		this.memberShipRepositoryJPA = memberShipRepoJpa;
	}
	
	@Transactional
	@Override
	public User save(User user) {
			UserEntity signedUserEntity = userRepositoryJPA.save(UserMapper.fromDomainToSchema(user));
			return UserMapper.fromSchemaToDomain(signedUserEntity);
	}
	
	@Override
	public boolean existUserByEmail(String email) {
		log.info("Buscando al usuario " + email);
		return userRepositoryJPA.findUserByEmail(email).isPresent();
	}
	
	@Override
	public User findUserByEmail(String email) {
		Optional<UserEntity> userEntity =  userRepositoryJPA.findUserByEmail(email);
		return UserMapper.fromSchemaToDomain(userEntity.orElseGet(()->null));
	}
	
	@Transactional
	@Override
	public void deleteAccount(String email) {
		int usedDelete = userRepositoryJPA.deleteByEmail(email);
		if (usedDelete == 0 ) {
			log.error("El usuario con email " + email + " no ha sido eliminado");
			throw new RuntimeException("El usuario con email " + email + " no ha sido eliminado");
		}
		
		log.info("Usuario con email " + email + " borrado con exito");
	}


	@Override
	public void saveRecoverPasswordVerifications(String email, String codeNumber, LocalDateTime expirationCodeDate) {
		UserEntity userEntity = userRepositoryJPA.findUserByEmail(email).orElseThrow(()->new RuntimeException());
		userEntity.setCodeNumber(codeNumber);
		userEntity.setCodeExpirationTime(expirationCodeDate);
		userRepositoryJPA.save(userEntity);

	}


	//Cambiar a societyAdapter
	@Override
	public List<Society> showMemberSociety(String email) {
		List<Society> list = new ArrayList<>();
		memberShipRepositoryJPA.findSocietyByUserEmail(email).forEach(society->{
			list.add(SocietyMapper.fromSchemaToDomain(society));
		});;
		
		return list;
	}

	@Override
	public void resetCodeNumberValue(String email) {
		UserEntity userEntity = userRepositoryJPA.findUserByEmail(email).orElseThrow(()->new RuntimeException());
		userEntity.setCodeNumber("");
		userEntity.setCodeExpirationTime(null);
		userRepositoryJPA.save(userEntity);	
	}

	@Override
	public User findUserById(Long userId) {
		UserEntity signedUserEntity = userRepositoryJPA.findById(userId).orElseGet(()->null);
		return UserMapper.fromSchemaToDomain(signedUserEntity);
	}
	

	

}
