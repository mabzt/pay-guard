package com.maison.mabs.userservice.infrastructure.config.jpa;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
}
