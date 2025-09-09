package es.uco.tfg.elderBridge.infrastructure.database.adapters.mappers;


import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;

@Component("adapterSocietyMapper")
public class SocietyMapper {
	public static Society fromSchemaToDomain(SocietyEntity societyEntity) {
		if (societyEntity == null) {
			return null;
		}
		
		return Society.
				builder().
				email(societyEntity.getEmail()).
				name(societyEntity.getName()).
				location(societyEntity.getLocation()).
				idUserCreator(societyEntity.getCreator().getUserId()).
				idSociety(societyEntity.getSocietyId()).
				description(societyEntity.getDescription()).
				build();
	}
	public static SocietyEntity fromDomainToSchema(Society society) {
		if (society == null) {
			return null;
		}
		
		return SocietyEntity.
				builder().
				email(society.getEmail()).
				name(society.getName()).
				location(society.getLocation()).
				//idUserCreator(society.getIdUserCreator()).
				societyId(society.getIdSociety()).
				description(society.getDescription()).
				build();
	}
	
	public static SocietyEntity fromDomainToSchema(Society society, UserEntity ownerCreator) {
		if (society == null) {
			return null;
		}
		
		return SocietyEntity.
				builder().
				email(society.getEmail()).
				name(society.getName()).
				location(society.getLocation()).
				creator(ownerCreator).
				societyId(society.getIdSociety()).
				description(society.getDescription()).
				build();
	}
}
