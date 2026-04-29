package com.internship.tool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables Spring Data JPA auditing.
 * Required for @CreatedDate and @LastModifiedDate to be populated automatically.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
