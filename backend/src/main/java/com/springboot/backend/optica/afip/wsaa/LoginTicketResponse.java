package com.springboot.backend.optica.afip.wsaa;

import lombok.Data;

@Data
public class LoginTicketResponse {
    private String token;
    private String sign;
    private String expirationTime;
}
