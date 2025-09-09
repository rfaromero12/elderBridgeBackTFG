package es.uco.tfg.elderBridge.domain.ports;

import java.time.LocalDateTime;
import java.util.List;

import es.uco.tfg.elderBridge.domain.models.Society;
import es.uco.tfg.elderBridge.domain.models.User;

public interface UserPort {
	public User save(User user);

	public void deleteAccount(String email);

	public List<Society> showMemberSociety(String email);
	
	public boolean existUserByEmail(String email);

	public User findUserByEmail(String email);

	public void saveRecoverPasswordVerifications(String email, String codeNumber, LocalDateTime expirationCodeDate);

	public void resetCodeNumberValue(String email);

	public User findUserById(Long userId);
}
