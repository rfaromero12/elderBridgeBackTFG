package es.uco.tfg.elderBridge.infrastructure.rest.dtos.file;

public record FileDTO(Long idFile,
 String name,
 String url,
 Long idSociety,
 String description) {

}
