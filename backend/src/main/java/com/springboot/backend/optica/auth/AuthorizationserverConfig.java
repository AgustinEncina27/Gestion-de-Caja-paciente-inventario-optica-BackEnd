package com.springboot.backend.optica.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import java.util.Arrays;


@Configuration
@EnableAuthorizationServer
public class AuthorizationserverConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtConfig jwtConfig; // Inyectamos JwtConfig

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		clients.inMemory().withClient("angularapp.optica")
		.secret(passwordEncoder.encode("ryq05GTa9sq>"))
		.scopes("read","write")
		.authorizedGrantTypes("password","refresh_token")
		.accessTokenValiditySeconds(5 * 60 * 60)
		.refreshTokenValiditySeconds(5 * 60 * 60);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	    TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
	    enhancerChain.setTokenEnhancers(
	        Arrays.asList(infoAdicionalToken, accessTokenConverter()) // El orden es importante
	    );

	    endpoints.authenticationManager(authenticationManager)
	             .tokenStore(tokenStore())
	             .accessTokenConverter(accessTokenConverter())
	             .tokenEnhancer(enhancerChain);
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
	    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
	    jwtAccessTokenConverter.setSigningKey(jwtConfig.getSecretKey()); // Usar clave desde configuración
	    return jwtAccessTokenConverter;
	}
	
	
}