package dbp.hackathon.Ticket;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class QRService {
    public String generarQR(String data) {
        try {
            // Codificar los datos para URL
            String encodedData = URLEncoder.encode(data, StandardCharsets.UTF_8.toString());
            return "https://api.qrserver.com/v1/create-qr-code/?size=400x400&data=" + encodedData;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error al generar QR", e);
        }
    }
}