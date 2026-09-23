package com.maison.mabs.userservice.infrastructure.out.persistence;

import com.maison.mabs.userservice.application.port.out.persistance.UserPersistencePort;
import com.maison.mabs.userservice.domain.model.User;
import com.maison.mabs.userservice.infrastructure.out.persistence.mapper.UserMapper;
import com.maison.mabs.userservice.infrastructure.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

	private final UserJpaRepository userJpaRepository;

	private final UserMapper userMapper;

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findUserByEmail(String email) {
		return this.userJpaRepository.findByEmail(email).map(this.userMapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findUserById(UUID id) {
		return this.userJpaRepository.findById(id).map(this.userMapper::toDomain);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User save(User user) {
		try {
			var userJpa = this.userMapper.toEntity(user);
			var savedUser = this.userJpaRepository.save(userJpa);
			return this.userMapper.toDomain(savedUser);
		}
		catch (DataIntegrityViolationException exception) {
			// Handle race conditions where two requests for the same user arrive at the
			// same time bypassing the findUserByEmail check
			throw new RuntimeException("User with email already exists"); // Todo: Add
																			// exception
																			// handling
		}
	}

}
