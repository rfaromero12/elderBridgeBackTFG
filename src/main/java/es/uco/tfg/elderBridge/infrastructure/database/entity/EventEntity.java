package es.uco.tfg.elderBridge.infrastructure.database.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EVENT")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eventId;
	@Column(unique = true)
	private String name;
	private LocalDateTime eventDate;
	private String description;
	private String eventLocation;
	
    @Builder.Default
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ParticipantEntity> participantEntities = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name="userCreator", nullable = false)
	private UserEntity userCreator;
	
	@ManyToOne
	@JoinColumn(name="societyOrganizer", nullable = false)
	private SocietyEntity societyOrganizer;
	
}
