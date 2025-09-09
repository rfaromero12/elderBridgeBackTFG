package es.uco.tfg.elderBridge.application.mapper;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.UserInput;
import es.uco.tfg.elderBridge.application.out.UserOutput;
import es.uco.tfg.elderBridge.domain.models.User;

@Component("applicationUserMapper")
public class UserMapper {

	public static UserOutput toOut(User user, String token) {
		if (user == null) {
			return null;
		}
		
		return UserOutput.
				builder().
				email(user.getEmail()).
				name(user.getName()).
				password(user.getPassword()).
				rol(user.getRol()).
				surname(user.getSurname()).
				userId(user.getUserId()).
				token(token).
				build();
	}
	
	public static UserOutput toOut(User user) {
		if (user == null) {
			return null;
		}
		
		return UserOutput.
				builder().
				email(user.getEmail()).
				name(user.getName()).
				password(user.getPassword()).
				rol(user.getRol()).
				surname(user.getSurname()).
				userId(user.getUserId()).
				build();
	}
	public static User fromInput(UserInput input) {
		if (input == null) {
			return null;
		}
		
		return User.
				builder().
				email(input.getEmail()).
				name(input.getName()).
				password(input.getPassword()).
				rol(input.getRol()).
				surname(input.getSurname()).
				userId(input.getUserId()).
				build();
	}
	
	
}
