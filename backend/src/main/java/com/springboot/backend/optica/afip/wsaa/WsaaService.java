package com.springboot.backend.optica.afip.wsaa;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WsaaService {

    private static final String SERVICE = "wsfe";
    private static final String WSAA_URL = "https://wsaahomo.afip.gov.ar/ws/services/LoginCms";

    // Ruta al archivo .jks, alias y contraseña (puede parametrizarse por cliente)
    private static final String JKS_PATH = "src/main/resources/certificados/certificado.p12";
    private static final String JKS_PASSWORD = "miclave123";
    private static final String JKS_ALIAS = "test";

    public LoginTicketResponse obtenerLoginTicket() throws Exception {
        // 1. Crear loginTicketRequest XML
        String loginXml = LoginTicketRequestGenerator.generate(SERVICE);

        // 2. Firmar con certificado p12
        byte[] signedCms = CmsSigner.sign(loginXml, JKS_PATH, JKS_PASSWORD, JKS_ALIAS);

        // 3. Enviar CMS al WSAA usando HttpClient
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(WSAA_URL))
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(signedCms))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // 4. Leer respuesta XML
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(response.body()));

        String token = doc.getElementsByTagName("token").item(0).getTextContent();
        String sign = doc.getElementsByTagName("sign").item(0).getTextContent();
        String expirationTime = doc.getElementsByTagName("expirationTime").item(0).getTextContent();

        LoginTicketResponse ticket = new LoginTicketResponse();
        ticket.setToken(token);
        ticket.setSign(sign);
        ticket.setExpirationTime(expirationTime);
        return ticket;
    }
}