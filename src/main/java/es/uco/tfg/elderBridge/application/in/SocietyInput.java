package es.uco.tfg.elderBridge.application.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SocietyInput {
	private String name;
	private String location;
	private Long idSociety;
	private String email;
	private Long idUserCreator;
	private String description;

}