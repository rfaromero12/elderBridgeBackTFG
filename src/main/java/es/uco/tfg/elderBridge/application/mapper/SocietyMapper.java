package es.uco.tfg.elderBridge.application.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.domain.models.Society;

@Component("applicationSocietyMapper")
public class SocietyMapper {
	public static SocietyOutput toOut(Society society) {
		if (society == null) {
			return null;
		}
		
		return SocietyOutput.
				builder().
				email(society.getEmail()).
				name(society.getName()).
				location(society.getLocation()).
				idUserCreator(society.getIdUserCreator()).
				idSociety(society.getIdSociety()).
				description(society.getDescription()).
				build();
	}
	public static Society fromInput(SocietyInput input) {
		if (input == null) {
			return null;
		}
		
		return Society.
				builder().
				email(input.getEmail()).
				name(input.getName()).
				location(input.getLocation()).
				idUserCreator(input.getIdUserCreator()).
				idSociety(input.getIdSociety()).
				description(input.getDescription()).
				build();
	}
	
	public static List<SocietyOutput> toOutList(List<Society> societyList) {
		if (societyList == null || societyList.isEmpty()) {
			return Collections.emptyList();
		}
		List<SocietyOutput> list = new ArrayList<>();
		societyList.forEach(society->{
			list.add(SocietyOutput.
					builder().
					email(society.getEmail()).
					name(society.getName()).
					location(society.getLocation()).
					idUserCreator(society.getIdUserCreator()).
					idSociety(society.getIdSociety()).
					description(society.getDescription()).
					build());
		});
		
		return list; 
	}
}
