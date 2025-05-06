package dbp.hackathon.Ticket;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCorreo(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // El true indica que es HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            // Puedes lanzar una excepción personalizada si lo prefieres
        }
    }
}
