package es.uco.tfg.elderBridge.application.usercases.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.mockito.ArgumentMatchers.any;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.domain.exceptions.DomainErrorList;
import es.uco.tfg.elderBridge.domain.exceptions.DomainException;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.FileAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.SocietyAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.FileEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.FilesEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.storeDocument.StoreDocumentService;
import es.uco.tfg.elderBridge.infrastructure.storeDocument.StoreFile;
import software.amazon.awssdk.http.HttpStatusFamily;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("Upload File tests")
public class UploadFileInteractorTest {

	private UploadFileInteractor uploadFileInteractor;
	
	@Mock
	private FilesEntityRepositoryJPA filesEntityRepositoryJPA;
	
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	private SocietyAdapter societyAdapter;
	private FileAdapter fileAdapter;
	
	@Mock
	private StoreFile storeFile;
	
	private FileEntity fileEntity;
	
	private FileInput fileInput;
	
	private SocietyEntity societyEntity;
	
	@Mock
	private S3Client s3Client; 
	
	private String destinationFolder = "src/main/resources/static/";
	@Mock
	PutObjectResponse putObjectResponse;
	private UserEntity userEntity;
	
	private Path pathFile;
	
	SdkHttpResponse response = SdkHttpResponse.builder().statusCode(HttpStatusFamily.SUCCESSFUL.ordinal()).build();
	
	@BeforeEach
	void setUp() {
		fileInput = FileInput.builder().
				description("ssdsdsds")
				.idFile(1L)
				.idSociety(1L)
				.name("FicheroTexto.pdf")
				.url("adsdsd")
				.build();
		 userEntity = UserEntity.builder()
	                .userId(1L)
	                .email("test@example.com")
		            .password("aaaaaa")
		            .name("Test")
		            .surname("User")
	                .build();
		societyEntity =	SocietyEntity.builder()
				.creator(userEntity)
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
		pathFile = Paths.get(destinationFolder);
		societyAdapter = new SocietyAdapter(societyRepositoryJPA, null, null);
		fileAdapter = new FileAdapter(filesEntityRepositoryJPA, storeFile, societyRepositoryJPA);
		uploadFileInteractor = new UploadFileInteractor(fileAdapter, societyAdapter);
	}
	
	
	@Test
	@DisplayName("1. Happy Case Login OK")
	void shouldUploadFileSuccesfully() throws Exception {
		when(filesEntityRepositoryJPA.save(any(FileEntity.class))).thenReturn(fileEntity);
		when(filesEntityRepositoryJPA.existsByNameAndSocietyId("FicheroTexto.pdf", 1L)).thenReturn(false);
		when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.of(societyEntity));
		when(storeFile.upload("FicheroTexto.pdf", pathFile, societyEntity.getName().toLowerCase())).thenReturn(true);
		
	
		
		
		uploadFileInteractor.doInteractor(fileInput,pathFile);
	
		
		
		
		
	    verify(societyRepositoryJPA, times(2)).findById(1L);
        verify(filesEntityRepositoryJPA,times(1)).save(any(FileEntity.class));
        verify(filesEntityRepositoryJPA,times(0)).delete(any(FileEntity.class));
		
	}
	@Test
	@DisplayName("2. File already exist")
	void fileAlreadyExist() throws IOException {
		when(filesEntityRepositoryJPA.existsByNameAndSocietyId("FicheroTexto.pdf", 1L)).thenReturn(true);

		DomainException exception = assertThrows(DomainException.class, 
	            () -> uploadFileInteractor.doInteractor(fileInput,pathFile));
	
		assertEquals(exception.getName(), DomainErrorList.NombreArchivoEnUso.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.NombreArchivoEnUso.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	    verify(filesEntityRepositoryJPA, times(1)).existsByNameAndSocietyId("FicheroTexto.pdf", 1L);
        
	}
	
	@Test
	@DisplayName("3. Error en base de datos al obtener los datos de la ong")
	void ongNotFound() throws IOException {
		societyEntity.setSocietyId(3L);
		fileEntity.setCreator(societyEntity);
		when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.empty());
		when(filesEntityRepositoryJPA.existsByNameAndSocietyId("FicheroTexto.pdf", 1L)).thenReturn(false);


		
		DomainException exception = assertThrows(DomainException.class, 
	            () -> uploadFileInteractor.doInteractor(fileInput,pathFile));
	
		assertEquals(exception.getName(), DomainErrorList.ONGNoEncontrada.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.ONGNoEncontrada.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
	    verify(filesEntityRepositoryJPA, times(1)).existsByNameAndSocietyId("FicheroTexto.pdf", 1L);
	    verify(societyRepositoryJPA, times(1)).findById(1L);
        
		
	}
	
	@Test
	@DisplayName("4.Error uploading to store ")
	void errorUploadingToStore() throws Exception {
		
		when(societyRepositoryJPA.findById(1L)).thenReturn(Optional.of(societyEntity));
		when(filesEntityRepositoryJPA.existsByNameAndSocietyId("FicheroTexto.pdf", 1L)).thenReturn(false);
		when(filesEntityRepositoryJPA.save(any(FileEntity.class))).thenReturn(fileEntity);
		
		DomainException exception = assertThrows(DomainException.class, 
	            () -> uploadFileInteractor.doInteractor(fileInput,pathFile));
	
		assertEquals(exception.getName(), DomainErrorList.SubidaArchivoErronea.getName());
		assertEquals(exception.getDescripcion(), DomainErrorList.SubidaArchivoErronea.getDescription());
		assertEquals(exception.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
	    verify(filesEntityRepositoryJPA, times(1)).existsByNameAndSocietyId("FicheroTexto.pdf", 1L);
	    verify(societyRepositoryJPA, times(2)).findById(1L);
        verify(filesEntityRepositoryJPA,times(1)).save(any(FileEntity.class));
        verify(filesEntityRepositoryJPA,times(1)).delete(any(FileEntity.class));

	}
}
