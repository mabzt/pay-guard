package com.maison.mabs.userservice.infrastructure.config.keycloak;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(String serverUrl, String realm, String clientId, String clientSecret, Admin admin) {
	@Builder
	public record Admin(String username, String password) {
	}
}
