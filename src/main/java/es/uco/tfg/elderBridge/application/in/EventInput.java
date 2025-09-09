package es.uco.tfg.elderBridge.application.in;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EventInput {
	private String name;
	private LocalDateTime eventDate;
	private String eventLocation;
	private String description;
	private Long eventId;
	private Long userId;
	private Long societyId;

}
