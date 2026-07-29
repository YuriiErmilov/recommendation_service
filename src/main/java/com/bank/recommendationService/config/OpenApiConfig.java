package com.bank.recommendationService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация документации REST API приложения.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Создаёт общее описание OpenAPI.
     *
     * @return конфигурация документации REST API
     */
    @Bean
    public OpenAPI recommendationServiceOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Recommendation Service API")
                                .version("1.0.0")
                                .description(
                                        """
                                        REST API сервиса персональных банковских рекомендаций.

                                        Сервис поддерживает:
                                        - получение рекомендаций;
                                        - управление динамическими правилами;
                                        - статистику срабатываний правил;
                                        - очистку кешей;
                                        - получение информации о приложении.
                                        """
                                )
                                .contact(
                                        new Contact()
                                                .name("Юрий")
                                )
                );
    }
}
