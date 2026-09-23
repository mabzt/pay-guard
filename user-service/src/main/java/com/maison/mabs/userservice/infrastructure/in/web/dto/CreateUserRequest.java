package com.maison.mabs.userservice.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateUserRequest(
//@formatter:off
		@NotEmpty(message = "User email address is required.")
		@Schema(description = "User email address", example = "johndoe@email.com")
		String email,

		@NotEmpty(message = "User password is required.")
		@Schema(description = "User password")
		String password,

		@NotNull(message = "Role is required")
		@Schema(description = "User role", allowableValues = {"USER", "ADMIN" }, example = "USER")
		UserRole Role) {

//@formatter:on
}
