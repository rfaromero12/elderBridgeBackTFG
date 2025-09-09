package es.uco.tfg.elderBridge.infrastructure.rest.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.uco.tfg.elderBridge.application.in.FileInput;
import es.uco.tfg.elderBridge.application.out.FileOutput;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.DownloadFileDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.FileDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.ResponseDownloadFile;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.ShowAvailablesFileDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.file.UploadFileDTO;



@Mapper(componentModel = "spring")
public interface FileMapper {
	FileInput fromDownloadFileDTOtoFileInput(DownloadFileDTO downloadFileDTO);

	FileInput fromUploadFileDTOtoFileInput(UploadFileDTO uploadFileDTO);

	FileInput fromShowAvailablesFileDTOtoFileInput(ShowAvailablesFileDTO request);

	List<FileDTO> fromFileOutputListToFileDTOList(List<FileOutput> doInteractor);

	ResponseDownloadFile fromFileOutputToResponseDownloadDTO(FileOutput doInteractor);

}
