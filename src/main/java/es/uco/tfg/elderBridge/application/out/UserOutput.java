package es.uco.tfg.elderBridge.application.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserOutput {
	private Long userId;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String rol;
	private String token;
}