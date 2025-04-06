package com.springboot.backend.optica.auth;

import java.util.HashMap;
import java.util.Map;

import com.springboot.backend.optica.modelo.Local;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

	@Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> info = new HashMap<>();

        Local local = userDetails.getLocal();

        if (local != null) {
            // Podés agregar solo el ID
            info.put("local_id", local.getId());

            // O incluso el objeto entero si querés más info (no recomendado si tiene relaciones)
            // Map<String, Object> localData = new HashMap<>();
            // localData.put("id", local.getId());
            // localData.put("nombre", local.getNombre());
            // info.put("local", localData);
        } else {
            info.put("local_id", null);
            info.put("local_nombre", null);
        }

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
