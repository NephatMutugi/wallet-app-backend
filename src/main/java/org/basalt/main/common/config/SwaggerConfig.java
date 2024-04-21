package org.basalt.main.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author Nephat Muchiri
 * Date 21/04/2024
 */
@Configuration
public class SwaggerConfig {
    @Bean
    GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info().title("WALLET APP").version("1.0"));
    }
}
