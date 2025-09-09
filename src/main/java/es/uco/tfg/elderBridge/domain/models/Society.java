package es.uco.tfg.elderBridge.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Society {
	private String name;
	private String location;
	private Long idSociety;
	private String email;
	private Long idUserCreator;
	private String description;
}
