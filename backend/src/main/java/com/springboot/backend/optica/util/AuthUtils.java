package com.springboot.backend.optica.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthUtils {

    @Autowired
    private TokenStore tokenStore;

    public Long obtenerLocalIdDesdeToken(Authentication authentication) {
        if (!(authentication instanceof OAuth2Authentication)) return null;

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String token = details.getTokenValue();

        DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) tokenStore.readAccessToken(token);
        Map<String, Object> info = accessToken.getAdditionalInformation();

        if (info.containsKey("local_id")) {
            return Long.valueOf(info.get("local_id").toString());
        }

        return null;
    }

    public String obtenerRolDesdeToken(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("");
    }
}
