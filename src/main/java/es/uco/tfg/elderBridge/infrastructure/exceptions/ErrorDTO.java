package es.uco.tfg.elderBridge.infrastructure.exceptions;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    @Schema(description = "Mensaje de error ", example = "El usuario no ha sido encontrado")
	@NonNull
	private String errorMessage;
}
