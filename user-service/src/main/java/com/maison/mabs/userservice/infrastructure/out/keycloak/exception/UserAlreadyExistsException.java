package com.maison.mabs.userservice.infrastructure.out.keycloak.exception;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
