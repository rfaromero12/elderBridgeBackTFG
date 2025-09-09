package es.uco.tfg.elderBridge.domain.ports;

import java.util.List;

import es.uco.tfg.elderBridge.domain.models.Society;

public interface SocietyPort {

	Society createSociety(Society fromInput);

	void deleteSociety(Society fromInput);
	
	List<Society> showSubSociety(String userEmail);

	void joinSociety(String userEmail, String societyName);

	void unjoinSociety(String userEmail, String societyName);

	List<Society> showAvailableSocieties(String idUserCreator);

	Society findById(Long idSociety);

	Society findSocietyByName(String name);

	Society findSocietyByEmail(String email);

	Society findSocietyByIdUserCreator(Long idUserCreator);

	Society update(Society fromInput);

}
