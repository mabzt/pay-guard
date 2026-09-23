package com.maison.mabs.userservice.infrastructure.out.keycloak.dto;

import lombok.Builder;

@Builder
public record AuthTokenResponse(String accessToken,

		String refreshToken,

		Long expiresIn,

		String tokenType) {
}
