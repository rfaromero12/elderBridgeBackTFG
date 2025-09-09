package es.uco.tfg.elderBridge.application.usercases.file;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.application.out.FileOutput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.FileAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.FileEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.FilesEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.storeDocument.StoreDocumentService;
import es.uco.tfg.elderBridge.infrastructure.storeDocument.StoreFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.ResponseBytes;

@ExtendWith(MockitoExtension.class)
@DisplayName("Download File tests")
public class DownloadFileInteractorTest {

	
	private DownloadFileInteractor downloadFileInteractor;
	
	@Mock
	private FilesEntityRepositoryJPA filesEntityRepositoryJPA;
	
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	private FileAdapter fileAdapter;
	@Mock
	private S3Client s3Client;
	
	private StoreFile storeFile;
	
	private FileEntity fileEntity;
	
	private FileInput fileInput;
	
	private SocietyEntity societyEntity;

	@Mock
	ResponseBytes<GetObjectResponse> getResponseBytes;
	@BeforeEach
	void setUp() {
		fileInput = FileInput.builder().
				description("ssdsdsds")
				.idFile(1L)
				.idSociety(1L)
				.name("FicheroTexto.pdf")
				.url("adsdsd")
				.build();
		societyEntity =	SocietyEntity.builder()
				.creator(null)
				.email("ejemplo@gmail.com")
				.societyId(1L)
				.location("locationEjemplo")
				.name("nombreEjemplo")
				.memberships(null)
				.description("description").build();	
		
		fileEntity = FileEntity.builder()
				.description("ssdsdsds")
				.creator(societyEntity)
				.name("FicheroTexto.pdf")
				.fileId(1L)
				.email("email@Adsad.com")
				.build();
		storeFile = new StoreDocumentService(s3Client);
		fileAdapter = new FileAdapter(filesEntityRepositoryJPA, storeFile, societyRepositoryJPA);
		downloadFileInteractor = new DownloadFileInteractor(fileAdapter);
	}
	
	
	@Test
	@DisplayName("1. Happy Case Login OK")
	void shouldDownloadFileSuccesfully() throws IOException {
		when(filesEntityRepositoryJPA.existsById(1L)).thenReturn(true);
		when(filesEntityRepositoryJPA.findById(1L)).thenReturn(Optional.of(fileEntity));
		when(getResponseBytes.asByteArray()).thenReturn("aaaaa".getBytes());
		when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(getResponseBytes);
		
		FileOutput result = downloadFileInteractor.doInteractor(fileInput);
		assertNotNull(result);
		assertNotNull(result.getFileContent());
		assertEquals(result.getIdFile(),1L);
		assertEquals(result.getIdSociety(),1L);
		assertEquals(result.getName(),"FicheroTexto.pdf");
		
	}
	@Test
	@DisplayName("2. File doesnt exist")
	void fileNotExists() throws IOException {
		when(filesEntityRepositoryJPA.existsById(1L)).thenReturn(false);
		
		DomainException exception = assertThrows(DomainException.class, 
	            () -> downloadFileInteractor.doInteractor(fileInput));
	
		assertEquals(exception.getName(), DomainErrorList.ArchivoNoEncontrado.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.ArchivoNoEncontrado.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	    verify(filesEntityRepositoryJPA, times(1)).existsById(1L);
        
	}
	
	@Test
	@DisplayName("3. Error downloading file from store ")
	void errorDownloadingFileFromStore() throws IOException {
		when(filesEntityRepositoryJPA.existsById(1L)).thenReturn(true);
		when(filesEntityRepositoryJPA.findById(1L)).thenReturn(Optional.of(fileEntity));
		when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenThrow(S3Exception.class);
		
		DomainException exception = assertThrows(DomainException.class, 
	            () -> downloadFileInteractor.doInteractor(fileInput));
	
		assertEquals(exception.getName(), DomainErrorList.DescargaArchivoFallida.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.DescargaArchivoFallida.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
	    verify(filesEntityRepositoryJPA, times(1)).findById(1L);
        
		
	}
	
}
