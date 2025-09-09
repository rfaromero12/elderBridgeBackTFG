package es.uco.tfg.elderBridge.infrastructure.database.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "PARTICIPANT")
public class ParticipantEntity {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long participantId;

	    @ManyToOne
	    @JoinColumn(name = "event_id", nullable = false)
	    private EventEntity event;

	    @ManyToOne
	    @JoinColumn(name = "member_id", nullable = false)
	    private UserEntity member;
}