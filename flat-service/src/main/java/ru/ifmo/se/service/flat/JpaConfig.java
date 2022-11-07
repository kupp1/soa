package ru.ifmo.se.service.flat;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.ifmo.se.common.AbstractJpaConfig;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.ifmo.se.service.flat.repository"})
public class JpaConfig extends AbstractJpaConfig {
}
