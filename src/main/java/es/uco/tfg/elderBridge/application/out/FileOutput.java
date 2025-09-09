package es.uco.tfg.elderBridge.application.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileOutput {
	private Long idFile;
	private String name;
	private String url;
	private Long idSociety;
	private String description;
	private byte[] fileContent;
}