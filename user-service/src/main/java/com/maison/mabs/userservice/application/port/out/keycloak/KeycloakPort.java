package com.maison.mabs.userservice.application.port.out.keycloak;

import com.maison.mabs.userservice.domain.model.Role;
import com.maison.mabs.userservice.domain.model.User;

public interface KeycloakPort {

	void createUser(User user, String password, Role role);
}
