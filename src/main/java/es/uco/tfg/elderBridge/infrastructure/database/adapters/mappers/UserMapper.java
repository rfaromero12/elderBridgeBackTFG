package es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.models.User;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;

@Component("adapterUserMapper")
public class UserMapper {
	public static User fromSchemaToDomain(UserEntity userEntity) {
		if (userEntity == null) {
			return null;
		}
		
		return User.
				builder().
				userId(userEntity.getUserId()).
				email(userEntity.getEmail()).
				name(userEntity.getName()).
				password(userEntity.getPassword()).
				surname(userEntity.getSurname()).
				codeNumber(userEntity.getCodeNumber()).
				codeExpirationTime(userEntity.getCodeExpirationTime()).
				build(); 
	}
	
	public static UserEntity fromDomainToSchema(User user) {
		if (user == null) {
			return null;
		}
		
		return UserEntity.
				builder().
				userId(user.getUserId()).
				email(user.getEmail()).
				name(user.getName()).
				password(user.getPassword()).
				surname(user.getSurname()).
				build(); 
	}
}
