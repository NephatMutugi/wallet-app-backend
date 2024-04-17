package org.basalt.main.common.config.entityaudit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditorConfig {
    @Bean(name = "auditorAware")
    AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }
}
