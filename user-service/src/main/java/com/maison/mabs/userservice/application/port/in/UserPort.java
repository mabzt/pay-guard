package com.maison.mabs.userservice.application.port.in;

import com.maison.mabs.userservice.domain.model.User;

import java.util.UUID;

public interface UserPort {

	User addUser(User addUserRequest);

	User updateUser(UUID id, User updateUserRequest);

}
