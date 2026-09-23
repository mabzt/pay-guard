package com.maison.mabs.userservice.infrastructure.out.persistence.mapper;

import com.maison.mabs.userservice.domain.model.User;
import com.maison.mabs.userservice.infrastructure.out.persistence.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	User toDomain(UserJpaEntity userJpaEntity);

	UserJpaEntity toEntity(User user);

}
