package es.uco.tfg.elderBridge.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{
		
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**") // Permitir todas las rutas
	         .allowedOrigins("http://localhost:5173") // Cambia esto a tu origen frontend
	         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	         .allowedHeaders("*") // Puedes añadir más cabeceras si es necesario
	         .allowCredentials(true);
	}
	
}
