package com.maison.mabs.userservice.infrastructure.out.keycloak;

import com.maison.mabs.userservice.application.port.out.keycloak.KeycloakPort;
import com.maison.mabs.userservice.domain.model.Role;
import com.maison.mabs.userservice.domain.model.User;
import com.maison.mabs.userservice.infrastructure.config.keycloak.KeycloakProperties;
import com.maison.mabs.userservice.infrastructure.out.keycloak.dto.AuthTokenResponse;
import com.maison.mabs.userservice.infrastructure.out.keycloak.exception.KeycloakException;
import com.maison.mabs.userservice.infrastructure.out.keycloak.exception.UserAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakAdapter implements KeycloakPort {

	private static final String LOCATION = "Location";

	private static final String USER_ID = "userId";

	private static final String ROLE = "role";

	private final KeycloakProperties keycloakProperties;

	private final RealmResource realmResource;

	@Override
	public void createUser(User user) {
		var keycloakUser = buildUserRepresentation(user);
		var keycloakUserId = createKeyCloakUserId(keycloakUser, user.email());
		assignRealmRole(keycloakUserId, user.role());
	}

	@Override
	public void revokeAllSessions(UUID id) {
		this.realmResource.users().get(findKeycloakUserId(id)).logout();
		log.info("Revoked all sessions for userId={}", id);
	}

	@Override
	public void deleteUser(UUID id) {
		String keycloakUserId = findKeycloakUserId(id);
		this.realmResource.users().get(keycloakUserId).remove();
		log.info("Keycloak user deleted for userId={}", id);
	}

	@Override
	public AuthTokenResponse obtainToken(String email, String password) {
		try {
			Keycloak keycloak = KeycloakBuilder.builder()
				.serverUrl(this.keycloakProperties.serverUrl())
				.realm(this.keycloakProperties.realm())
				.clientId(this.keycloakProperties.clientId())
				.clientSecret(this.keycloakProperties.clientSecret())
				.username(email)
				.password(password)
				.grantType(OAuth2Constants.PASSWORD)
				.build();

			AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();

			return AuthTokenResponse.builder()
				.accessToken(tokenResponse.getToken())
				.refreshToken(tokenResponse.getRefreshToken())
				.expiresIn(tokenResponse.getExpiresIn())
				.tokenType(tokenResponse.getTokenType())
				.build();

		}
		catch (KeycloakException exception) {
			throw new KeycloakException("Exception occurred obtaining token for email", exception.getCause());
		}
	}

	private UserRepresentation buildUserRepresentation(User user) {
		var credentialsRepresentation = new CredentialRepresentation();
		credentialsRepresentation.setTemporary(Boolean.FALSE);
		credentialsRepresentation.setType(CredentialRepresentation.PASSWORD);
		credentialsRepresentation.setValue(user.password());

		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put(ROLE, List.of(user.role().name()));
		attributes.put(USER_ID, List.of(user.id().toString()));

		return createUserRepresentation(user, attributes, credentialsRepresentation);
	}

	private static @NonNull UserRepresentation createUserRepresentation(User user, Map<String, List<String>> attributes,
			CredentialRepresentation credentialsRepresentation) {
		var userRepresentation = new UserRepresentation();
		userRepresentation.setUsername(user.email());
		userRepresentation.setEmail(user.email());
		userRepresentation.setEnabled(Boolean.TRUE);
		// Todo: Send email verification link and setEnabled and setEmailVerified to false
		// by default
		userRepresentation.setEmailVerified(Boolean.TRUE);
		userRepresentation.setAttributes(attributes);
		userRepresentation.setCredentials(List.of(credentialsRepresentation));
		userRepresentation.setRealmRoles(List.of(user.role().name()));
		return userRepresentation;
	}

	private String createKeyCloakUserId(UserRepresentation userRepresentation, String email) {
		try (Response response = this.realmResource.users().create(userRepresentation)) {
			return switch (response.getStatus()) {
				case 201 -> {
					var location = response.getHeaderString(LOCATION);
					var keycloakUserId = location.substring(location.lastIndexOf("/") + 1);
					log.info("Keycloak user created with id={} for user={}", keycloakUserId, email);
					yield keycloakUserId;
				}

				case 409 -> throw new UserAlreadyExistsException("User with email %s already exists" + email);

				default -> throw new KeycloakException("Failed to create keycloak user with status=%d email=%s"
					.formatted(response.getStatus(), email));
			};
		}
	}

	private void assignRealmRole(String keycloakUserId, Role role) {
		try {
			var realmRole = this.realmResource.roles().get(role.name()).toRepresentation();
			this.realmResource.users().get(keycloakUserId).roles().realmLevel().add(List.of(realmRole));
			log.info("Role={} assigned to keycloakUserId={}", role.name(), keycloakUserId);
		}
		catch (KeycloakException exception) {
			throw new KeycloakException(
					"Exception occurred assigning realm role with message " + exception.getMessage());
		}
	}

	private String findKeycloakUserId(UUID userId) {
		return this.realmResource.users()
			.searchByAttributes("userId:" + userId)
			.stream()
			.findFirst()
			.map(UserRepresentation::getId)
			.orElseThrow(() -> new KeycloakException("Keycloak user not found for userId=" + userId));
	}

}
