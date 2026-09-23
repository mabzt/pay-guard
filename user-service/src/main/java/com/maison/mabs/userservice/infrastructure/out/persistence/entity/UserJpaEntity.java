package com.maison.mabs.userservice.infrastructure.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "email_constraint", columnNames = "email") },
		indexes = { @Index(name = "email_idx", columnList = "email") })
public class UserJpaEntity extends BaseEntity {

	@Id
	private UUID id;

	private String email;

	private String password;

}
