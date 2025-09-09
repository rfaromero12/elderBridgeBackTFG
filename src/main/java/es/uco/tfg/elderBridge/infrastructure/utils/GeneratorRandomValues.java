package es.uco.tfg.elderBridge.infrastructure.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import es.uco.tfg.elderBridge.domain.service.GeneratorValue;

@Component
public class GeneratorRandomValues implements GeneratorValue{
	@Override
	public String generateCodeNumberVerification() {
		return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
	}

	@Override
	public String generateRandomNameToBucket() {
		return "";
	}
	
	
}
