package es.uco.tfg.elderBridge.infrastructure.rest.dtos.event;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EventDTO {
	@NotBlank(message = "El nombre del evento es obligatorio")
	private String name;
	private LocalDateTime eventDate;
	private String eventLocation;
	private String description;
	private Long eventId;
	private Long creatorId;
	private Long societyOrganizerId;
}
