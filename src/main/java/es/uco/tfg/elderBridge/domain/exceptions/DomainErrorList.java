package es.uco.tfg.elderBridge.domain.exceptions;

public enum DomainErrorList {

	UsuarioNoEncontrado("Usuario no encontrado", "El email informado no esta siendo usado por ningun usuario"),
	PasswordIncorrecta("Contraseña incorrecta","La contraseña no está asociado al email informado"),
	EmailInexistente("Email inexistente","El email no está vinculado a ninguna cuenta"),
	PasswordsNoCoinciden("Password no coinciden", "Las contraseñas no coinciden"),
	CodigoNumericoIncorrecto("Codigo numerico incorrecto","El codigo numerico no coincide con el generado"),
	EmailEnUso("Email en uso","Ya existe un usuario con ese email registrado"),
	NombreEventoEnUso("Nombre en uso","El nombre del evento ya esta en uso"),
	FechasInvalidas("Fecha invalida","La fecha del evento no puede ser anterior a la fecha actual"), 
	NombreOngEnUso("Nombre en uso", "El nomnbre de la ONG ya esta en uso"),
	EventoNoEncontrado("Evento no encontrado", "El nombre del evento informado no existe"), 
	ONGNoEncontrada("ONG no encontrada","El nombre de la ong informada no existe"), 
	CodigoExpirado("Codigo expirado","El codigo de recuperación ha expirado su tiempo"),
	ArchivoNoEncontrado("Archivo no encontrado", "El archivo informado no se ha encontrado"),
	SubidaArchivoErronea("Subida Erronea","Ha habido un problema a la hora de subir el archivo"),
	DescargaArchivoFallida("Descarga fallida", "Ha habido un problema a la hora de descargar el archivo"),
	LimiteOngsPorUsuarioSuperado("Limite de ong por usuario superado","Un usuario no puede ser creador de mas de una ong"),
	NombreArchivoEnUso("Nombre Archivo en uso", "Ya existe un archivo en esta ong con el mismo nombre");
	
	
	private String name;
	private	String description; 

	
	private DomainErrorList(String name,String descripcion) {
		this.name = name;
		this.description = descripcion;
	}


	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
}
