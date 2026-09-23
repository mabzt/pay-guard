package com.maison.mabs.userservice.application.port.out.keycloak;

import com.maison.mabs.userservice.domain.model.User;
import com.maison.mabs.userservice.infrastructure.out.keycloak.dto.AuthTokenResponse;

import java.util.UUID;

public interface KeycloakPort {

	void createUser(User user);

	void revokeAllSessions(UUID id);

	void deleteUser(UUID id);

	AuthTokenResponse obtainToken(String email, String password);

}
