package es.uco.tfg.elderBridge.infrastructure.rest.dtos.file;

public record ResponseDownloadFile(byte[] fileContent, String name) {

}
