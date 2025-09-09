package es.uco.tfg.elderBridge.domain.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
	private Long userId;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String rol;
	private String codeNumber;
	private LocalDateTime codeExpirationTime;
}
