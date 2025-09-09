package es.uco.tfg.elderBridge.infrastructure.mail;



import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class MailService {
	
	private JavaMailSender javaMailSender;
	
	private static String fromEmail = "elderBridge2324@gmail.com";
	
	
	public MailService(JavaMailSender mailInterface) {
		this.javaMailSender = mailInterface;
	}
	
	public void sendEmail(SimpleMailMessage mailMessage) {
		mailMessage.setFrom(fromEmail);
		log.info("Enviando correo");
		javaMailSender.send(mailMessage);
		
	}
	
	public void sendMultiplesEmail(List<String> userParticipants, SimpleMailMessage mailMessage) {
		mailMessage.setFrom(fromEmail);
		mailMessage.setBcc(userParticipants.toArray(new String[0]));
		
		javaMailSender.send(mailMessage);
	}
}
