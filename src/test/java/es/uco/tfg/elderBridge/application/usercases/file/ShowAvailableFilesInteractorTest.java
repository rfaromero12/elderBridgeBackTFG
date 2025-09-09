package es.uco.tfg.elderBridge.application.usercases.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.application.out.FileOutput;
import es.uco.tfg.elderBridge.infrastructure.database.adapters.FileAdapter;
import es.uco.tfg.elderBridge.infrastructure.database.entity.FileEntity;
import es.uco.tfg.elderBridge.infrastructure.database.entity.SocietyEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.FilesEntityRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.database.repository.SocietyRepositoryJPA;
import es.uco.tfg.elderBridge.infrastructure.storeDocument.StoreFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("Show Available Files tests")
public class ShowAvailableFilesInteractorTest {

	
	private ShowAvailableFilesInteractor showAvailableFilesInteractor;	

	@Mock
	private FilesEntityRepositoryJPA filesEntityRepositoryJPA;
	
	@Mock
	private SocietyRepositoryJPA societyRepositoryJPA;
	
	private FileAdapter fileAdapter;
	@Mock
	private StoreFile storeFile;
	
	private List<FileEntity> fileEntity;
	
	private FileInput fileInput;
	
	private SocietyEntity societyEntity;
	
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
		fileEntity = List.of(FileEntity.builder()
				.description("ssdsdsds")
				.creator(societyEntity)
				.name("FicheroTexto.pdf")
				.fileId(1L)
				.email("email@Adsad.com")
				.build(),
				FileEntity.builder()
				.description("ssdsdsds")
				.creator(societyEntity)
				.name("FicheroTextooo.pdf")
				.fileId(2L)
				.email("email@Adsad.com")
				.build());
		
		fileAdapter = new FileAdapter(filesEntityRepositoryJPA, storeFile, societyRepositoryJPA);
		showAvailableFilesInteractor = new ShowAvailableFilesInteractor(fileAdapter);
	}
	
	
	@Test
	@DisplayName("1. Happy Case Login OK")
	void shouldShowAvailableFilesSuccesfully() throws IOException {
		when(filesEntityRepositoryJPA.findFilesBySociety(1L)).thenReturn((fileEntity));
		
		List<FileOutput> result = showAvailableFilesInteractor.doInteractor(fileInput);
	
		assertEquals(result.size(), 2);

		assertNotNull(result);
		assertEquals(result.get(0).getIdFile(),1L);
		assertEquals(result.get(0).getIdSociety(),1L);
		assertEquals(result.get(0).getName(),"FicheroTexto.pdf");
		
	}
	@Test
	@DisplayName("2. File doesnt exist")
	void fileNotExists() throws IOException {
		
		List<FileOutput> result = showAvailableFilesInteractor.doInteractor(fileInput);

		assertEquals(result.size(), 0);
	
	    verify(filesEntityRepositoryJPA, times(1)).findFilesBySociety(1L);
        
	}
}
