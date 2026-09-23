package com.maison.mabs.userservice.infrastructure.config.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

	private final KeycloakProperties keycloakProperties;

	@Bean
	public Keycloak keycloakAdminClient() {
		return KeycloakBuilder.builder()
			.serverUrl(this.keycloakProperties.serverUrl())
			.realm("master")
			.clientId("admin-cli")
			.username(this.keycloakProperties.admin().username())
			.password(this.keycloakProperties.admin().password())
			.grantType(OAuth2Constants.PASSWORD)
			.build();
	}

	@Bean
	public RealmResource realmResource(Keycloak keycloak) {
		return keycloak.realm(this.keycloakProperties.realm());
	}

}
