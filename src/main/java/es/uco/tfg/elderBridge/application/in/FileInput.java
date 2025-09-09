package es.uco.tfg.elderBridge.application.in;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileInput {
	private String name;
	private String url;
	private Long idSociety;
	private String description;
	private Long idFile;
}
