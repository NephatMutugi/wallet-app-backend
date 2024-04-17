package org.basalt.main.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String allowedSystems;
    private String username;
    private String password;
}
