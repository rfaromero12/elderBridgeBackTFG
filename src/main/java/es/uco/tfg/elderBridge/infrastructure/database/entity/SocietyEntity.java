package es.uco.tfg.elderBridge.infrastructure.database.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "SOCIETY")
public class SocietyEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long societyId;
	@Column(name = "name", unique = true)
	private String name;
	private String location;
	@Column(unique = true)
	private String email;
	private String description;

	@OneToOne(mappedBy = "createdOrganization")
	private UserEntity creator;

    @Builder.Default
	@OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MembershipEntity> memberships = new ArrayList<>();
	
	//Lo nuevo
    @Builder.Default
	@OneToMany(mappedBy="creator", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<FileEntity> availablesFiles = new ArrayList<>();
	//nuevo
    @Builder.Default
	@OneToMany(mappedBy="societyOrganizer", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<EventEntity> availablesEvent = new ArrayList<>();
	
	
}
