package es.uco.tfg.elderBridge.infrastructure.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import es.uco.tfg.elderBridge.domain.service.CryptoService;

@Service
public class BCryptServiceImpl implements CryptoService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BCryptServiceImpl() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(4) ;
    }

    @Override
    public String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean verifyPassword(String password, String hashedPassword) {
        return bCryptPasswordEncoder.matches(password, hashedPassword);
    }
}