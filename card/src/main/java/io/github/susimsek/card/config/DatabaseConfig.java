package io.github.susimsek.card.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = "io.github.susimsek.card.repository")
@EnableJpaAuditing(
    dateTimeProviderRef = "dateTimeProvider",
    auditorAwareRef = "auditAwareImpl")
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public DateTimeProvider dateTimeProvider(Clock clock)  {
        return () -> Optional.of(Instant.now(clock));
    }
}
