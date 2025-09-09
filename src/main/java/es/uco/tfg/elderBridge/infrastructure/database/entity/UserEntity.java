package es.uco.tfg.elderBridge.infrastructure.database.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;



@Entity(name = "USER")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	private String name;
	private String surname;
	@Column(unique = true)
	private String email;
	private String password;
	//private String rol;
	private String codeNumber;
	private LocalDateTime codeExpirationTime;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "societyId")
	private SocietyEntity createdOrganization;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MembershipEntity> memberships;
	
	//Las participaciones del usuario en los eventos
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParticipantEntity> participantEntities;

	@OneToMany(mappedBy = "userCreator", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventEntity> eventCreatedBy;
}

