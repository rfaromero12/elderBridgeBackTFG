package es.uco.tfg.elderBridge.domain.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

	public String getToken(UserDetails user);
}
