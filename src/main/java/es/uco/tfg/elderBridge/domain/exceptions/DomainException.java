package es.uco.tfg.elderBridge.domain.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException{

	/**
	 * 
	 */
	
	private String name;
	private String descripcion;
	private HttpStatus status;
	
	public DomainException(DomainErrorList error, HttpStatus status) {
		this.name = error.getName();
		this.descripcion = error.getDescription();
		this.status = status;
	}
	private static final long serialVersionUID = 1L;

}
