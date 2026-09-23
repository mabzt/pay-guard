package com.maison.mabs.userservice.infrastructure.in.web.dto;

import com.maison.mabs.userservice.domain.model.Role;
import lombok.Builder;
import org.apache.kafka.common.Uuid;

@Builder
public record CreateUserResponse(Uuid id, String email, Role role) {
}
