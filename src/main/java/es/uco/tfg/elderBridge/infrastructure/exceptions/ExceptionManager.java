package es.uco.tfg.elderBridge.infrastructure.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.uco.tfg.elderBridge.domain.exceptions.DomainException;

@RestControllerAdvice
public class ExceptionManager {
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
			List<String> mensajes = ex.getBindingResult()
		        .getFieldErrors()
		        .stream()
		        .map(fieldError -> fieldError.getDefaultMessage())
		        .collect(Collectors.toList());

		    String errorFinal = String.join(" | ", mensajes);


        return ResponseEntity.badRequest().body(new ErrorDTO(errorFinal));
	}
	
	@ExceptionHandler(exception = DomainException.class)
	@ResponseBody
	public ResponseEntity<ErrorDTO> handleDomainException(DomainException exception) {
		
		return new ResponseEntity<>(new ErrorDTO(exception.getDescripcion()), exception.getStatus());
	}
	
	@ExceptionHandler(exception = RuntimeException.class)
	@ResponseBody
	public ResponseEntity<ErrorDTO> handleRuntimeExcep(RuntimeException e){
		
		return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(exception = Exception.class)
	@ResponseBody
	public ResponseEntity<ErrorDTO> handleException(Exception e){
		String errorMessage ="Ha ocurrido el siguiente error " + e.getMessage(); 
		
		return new ResponseEntity<>(new ErrorDTO(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
