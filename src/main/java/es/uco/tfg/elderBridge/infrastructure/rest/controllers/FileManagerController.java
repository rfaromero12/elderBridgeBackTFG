package es.uco.tfg.elderBridge.infrastructure.rest.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.uco.tfg.elderBridge.application.usercases.file.DownloadFileInteractor;
import es.uco.tfg.elderBridge.application.usercases.file.ShowAvailableFilesInteractor;
import es.uco.tfg.elderBridge.application.usercases.file.UploadFileInteractor;
import es.uco.tfg.elderBridge.infrastructure.exceptions.ErrorDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.DownloadFileDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.FileDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.ResponseDownloadFile;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.ShowAvailablesFileDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.UploadFileDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.mappers.FileMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/services/files")
public class FileManagerController {
	
	@Autowired
	private ShowAvailableFilesInteractor showAvailableFilesInteractor;
	
	@Autowired
	private UploadFileInteractor uploadFileInteractor;
	
	@Autowired
	private DownloadFileInteractor downloadFileInteractor;
	
	@Value("${spring.destination.folder}")
	private String destinationFolder;
	
	@Autowired
	private FileMapper fileMapper;
	

	@Operation(summary = "Descargar un archivo", description = "Endpoint que permite descargar un archivo"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El archivo ha sido descargado correctamente"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping("/download")
	public ResponseEntity<byte[]> downloadFile(@Valid @RequestBody DownloadFileDTO request){
		ResponseDownloadFile response = fileMapper.fromFileOutputToResponseDownloadDTO(downloadFileInteractor.doInteractor(fileMapper.fromDownloadFileDTOtoFileInput(request)));
	    String safeFileName = URLEncoder.encode(response.name(), StandardCharsets.UTF_8).replace("+", "%20");

		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + safeFileName + "\"; filename*=UTF-8''" + safeFileName)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .contentLength(response.fileContent().length)
	            .body(response.fileContent());
	}
	

	@Operation(summary = "Mostrar archivos disponibles", description = "Endpoint que permite mostrar archivos disponibles"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "La consulta ha sido un exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping("/showAvailableFile")
	public ResponseEntity<List<FileDTO>> showAvailableFiles(@Valid @RequestBody ShowAvailablesFileDTO request){
		List<FileDTO> list = fileMapper.fromFileOutputListToFileDTOList(showAvailableFilesInteractor.doInteractor(fileMapper.fromShowAvailablesFileDTOtoFileInput(request)));
		
		return ResponseEntity.ok(list);

	}

	@Operation(summary = "Subir un archivo", description = "Endpoint que permite subir un archivo a la nube"	)
	 @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "El archivo ha sido registrado con exito"),
	        @ApiResponse(responseCode = "400", content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = ErrorDTO.class)
	            )),
	})
	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam("fileName") String fileName,
			@RequestParam("idSociety") Long idSociety,@RequestParam("fileType") String fileType,
			@RequestParam("fileSize") String fileSize, @RequestPart("file") MultipartFile file) throws IOException{
		
		
		try {
            Path staticDir = Paths.get(destinationFolder);
            if (!Files.exists(staticDir)) {
                Files.createDirectories(staticDir);
            }

            Path filePath = staticDir.resolve(file.getOriginalFilename());
            Path finalPath = Files.write(filePath, file.getBytes());

    		UploadFileDTO uploadFileDTO  = new UploadFileDTO(fileName, finalPath, idSociety);

    		uploadFileInteractor.doInteractor(fileMapper.fromUploadFileDTOtoFileInput(uploadFileDTO), finalPath);

            Files.delete(filePath);
            
        } catch (IOException e) {
            throw new IOException("Error al procesar el archivo.");
        }
		
		return ResponseEntity.ok("OK");

	}
	
	
	
	
	
}
