package com.maison.mabs.userservice.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record User(UUID id, String email, String password, Role role) {
}
