package dbp.hackathon.Ticket;

import dbp.hackathon.Estudiante.Estudiante;
import dbp.hackathon.Estudiante.EstudianteRepository;
import dbp.hackathon.Funcion.Funcion;
import dbp.hackathon.Funcion.FuncionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private FuncionRepository funcionRepository;

    @Autowired
    private QRService qrService;  // Inyectamos el servicio de QR

    @Autowired
    private MailService mailService;  // Inyectamos el servicio de correo

    @Autowired
    private TemplateEngine templateEngine;

    private String generarPlantillaCorreo(Ticket ticket, String qrCodeUrl) {
        DecimalFormat df = new DecimalFormat("#.00");
        String total = df.format(ticket.getCantidad() * ticket.getFuncion().getPrecio());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy 'a las' h:mm a");
        String fechaFormateada = ticket.getFuncion().getFecha().format(formatter);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Confirmación de compra</title>" +
                "    <style>" +
                "        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { color: #2c3e50; text-align: center; border-bottom: 1px solid #eee; padding-bottom: 10px; }" +
                "        .details { margin: 20px 0; }" +
                "        .detail-item { margin-bottom: 10px; }" +
                "        .qr-container { text-align: center; margin: 25px 0; }" +
                "        .qr-code { border: 1px solid #ddd; padding: 10px; display: inline-block; }" +
                "        .footer { margin-top: 20px; font-size: 0.9em; color: #7f8c8d; text-align: center; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <h1 class=\"header\">¡Confirmación de compra!</h1>" +
                "    <p>Hola " + ticket.getEstudiante().getName() + ",</p>" +
                "    <p>Aquí tienes los detalles de tu compra:</p>" +
                "    <div class=\"details\">" +
                "        <div class=\"detail-item\"><strong>Evento:</strong> " + ticket.getFuncion().getNombre() + "</div>" +
                "        <div class=\"detail-item\"><strong>Fecha:</strong> " + fechaFormateada + "</div>" +
                "        <div class=\"detail-item\"><strong>Cantidad:</strong> " + ticket.getCantidad() + " entrada(s)</div>" +
                "        <div class=\"detail-item\"><strong>Total:</strong> $" + total + "</div>" +
                "    </div>" +
                "    <div class=\"qr-container\">" +
                "        <div class=\"qr-code\">" +
                "            <img src=\"" + qrCodeUrl + "\" alt=\"Código QR para entrada\" width=\"200\"/>" +
                "            <p>ID de ticket: " + ticket.getId() + "</p>" +
                "        </div>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>Presenta este código QR en la entrada del evento.</p>" +
                "        <p>¡Esperamos que disfrutes la función!</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
    public Ticket createTicket(Long estudianteId, Long funcionId, Integer cantidad) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
        Funcion funcion = funcionRepository.findById(funcionId).orElse(null);
        if (estudiante == null || funcion == null) {
            throw new IllegalStateException("Estudiante or Funcion not found!");
        }

        Ticket ticket = new Ticket();
        ticket.setEstudiante(estudiante);
        ticket.setFuncion(funcion);
        ticket.setCantidad(cantidad);
        ticket.setEstado(Estado.VENDIDO);
        ticket.setFechaCompra(LocalDateTime.now());

        // Guardar el ticket en la base de datos
        Ticket savedTicket = ticketRepository.save(ticket);

        // Generar el código QR usando el servicio de QR
        String qrCodeUrl = qrService.generarQR(savedTicket.getId().toString());  // Generamos el QR basado en el ID del ticket
        ticket.setQr(qrCodeUrl); // Guardamos el URL del QR en el ticket
        ticketRepository.save(ticket);


        // Enviar correo con el ticket y el QR
        String emailBody = generarPlantillaCorreo(savedTicket, qrCodeUrl);
        mailService.enviarCorreo(estudiante.getEmail(), "Confirmación de compra", emailBody);

        return savedTicket;
    }

    // Método auxiliar para generar el cuerpo del correo


    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    public Iterable<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Iterable<Ticket> findByEstudianteId(Long estudianteId) {
        return ticketRepository.findByEstudianteId(estudianteId);
    }

    public void changeState(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null) {
            throw new IllegalStateException("Ticket no encontrado");
        }
        if (ticket.getEstado() == Estado.CANJEADO) {
            throw new IllegalStateException("El ticket ya ha sido canjeado");
        }

        // Cambiar el estado a CANJEADO
        ticket.setEstado(Estado.CANJEADO);
        ticketRepository.save(ticket);

        // Enviar correo de confirmación de canje
        String emailBody = "Tu ticket para la función " + ticket.getFuncion().getNombre() + " ha sido canjeado exitosamente.";
        mailService.enviarCorreo(ticket.getEstudiante().getEmail(), "Confirmación de canjeo", emailBody);
    }

}