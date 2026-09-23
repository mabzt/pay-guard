package com.maison.mabs.userservice.infrastructure.out.keycloak.exception;

public class KeycloakException extends RuntimeException {

	public KeycloakException(String message) {
		super(message);
	}

	public KeycloakException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
