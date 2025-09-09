package es.uco.tfg.elderBridge.application.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SocietyOutput {
	private String name;
	private String location;
	private Long idSociety;
	private String email;
	private Long idUserCreator;
	private String description;
}
