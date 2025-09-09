package es.uco.tfg.elderBridge.infrastructure.rest.mappers;


import java.util.List;
import org.mapstruct.Mapper;

import es.uco.tfg.elderBridge.application.in.SocietyInput;
import es.uco.tfg.elderBridge.application.out.SocietyOutput;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.event.ResponseCreateEventDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.CreateSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.DeleteSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.EditSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.ResponseEditSocietyDTO;
import es.uco.tfg.elderBridge.infrastructure.rest.dtos.society.SocietyDTO;

@Mapper(componentModel = "spring")
public interface SocietyMapper {
	public SocietyInput fromSocietyDTOtoSocietyInput(SocietyDTO request);
	public SocietyDTO fromSocietyOutputToSocietyDTO(SocietyOutput societyOutput);
	public List<SocietyDTO> fromSocietyOutputToSocietyDTO(List<SocietyOutput> listSocietyOutput);
	
	SocietyInput fromCreateSocietyDTOtoSocietyInput(CreateSocietyDTO createSocietyDTO);
	SocietyInput fromDeleteSocietyDTOtoSocietyInput(DeleteSocietyDTO deleteSocietyDTO);
	SocietyInput fromEditSocietyDTOtoSocietyInput(EditSocietyDTO deleteSocietyDTO);

	
	ResponseCreateEventDTO fromSocietyOutputToResponseCreateSocietyDTO(SocietyOutput societyOutput);
	ResponseEditSocietyDTO fromSocietyOutputToResponseEditSocietyDTO(SocietyOutput societyOutput);
	
	
}
