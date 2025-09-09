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
@Entity(name = "MEMBERSHIP")

public class MembershipEntity {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long memberShipId;

	    @ManyToOne
	    @JoinColumn(name = "society_id", nullable = false)
	    private SocietyEntity society;

	    @ManyToOne
	    @JoinColumn(name = "member_id", nullable = false)
	    private UserEntity member;
}
