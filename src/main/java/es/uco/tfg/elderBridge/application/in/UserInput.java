package es.uco.tfg.elderBridge.application.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserInput {
	private Long userId;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String rol;
	private String confirmPassword;
	private String codeNumber;
}