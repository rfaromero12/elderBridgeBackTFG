package es.uco.tfg.elderBridge.domain.service;

public interface CryptoService {
    String hashPassword(String password);
    boolean verifyPassword(String password, String hashedPassword);
}