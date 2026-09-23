package com.maison.mabs.userservice.application.port.out.persistance;

import com.maison.mabs.userservice.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {

	Optional<User> findUserByEmail(String email);

	Optional<User> findUserById(UUID id);

	User save(User user);

}
