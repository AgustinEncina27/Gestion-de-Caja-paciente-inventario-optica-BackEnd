package com.springboot.backend.optica.afip.wsaa;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LoginTicketRequestGenerator {

    public static String generate(String service) {
        String uniqueId = String.valueOf(System.currentTimeMillis() / 1000L);
        String generationTime = ZonedDateTime.now().minusMinutes(10)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String expirationTime = ZonedDateTime.now().plusMinutes(10)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<loginTicketRequest version=\"1.0\">\n" +
                "  <header>\n" +
                "    <uniqueId>" + uniqueId + "</uniqueId>\n" +
                "    <generationTime>" + generationTime + "</generationTime>\n" +
                "    <expirationTime>" + expirationTime + "</expirationTime>\n" +
                "  </header>\n" +
                "  <service>" + service + "</service>\n" +
                "</loginTicketRequest>";
    }
}
