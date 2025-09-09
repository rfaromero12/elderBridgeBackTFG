package es.uco.tfg.elderBridge.infrastructure.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;

public interface UserRepositoryJPA extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findUserByEmail(String email);
	
	int deleteByEmail(String email);
	
}
