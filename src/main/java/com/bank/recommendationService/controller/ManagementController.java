package com.bank.recommendationService.controller;

import com.bank.recommendationService.dto.ServiceInfoResponse;
import com.bank.recommendationService.service.ManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер технического управления приложением.
 */
@RestController
@RequestMapping("/management")
@Tag(
        name = "Управление приложением",
        description = "Очистка кешей и получение информации о сервисе"
)
public class ManagementController {

    private final ManagementService managementService;

    public ManagementController(
            ManagementService managementService
    ) {
        this.managementService = managementService;
    }

    /**
     * Очищает кеши репозитория рекомендаций.
     *
     * @return ответ без тела
     */
    @Operation(
            summary = "Очистить кеши",
            description = """
                    Очищает кеш наличия продуктов,
                    количества транзакций и сумм транзакций.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "Все кеши очищены"
    )
    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        managementService.clearCaches();

        return ResponseEntity.noContent().build();
    }

    /**
     * Возвращает название и версию приложения.
     *
     * @return информация о сборке
     */
    @Operation(
            summary = "Получить информацию о сервисе",
            description = """
                    Возвращает название и версию,
                    сформированные Maven-плагином build-info.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Информация о сервисе получена",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ServiceInfoResponse.class
                    ),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "name": "recommendationService",
                                      "version": "0.0.1-SNAPSHOT"
                                    }
                                    """
                    )
            )
    )
    @GetMapping("/info")
    public ResponseEntity<ServiceInfoResponse> getServiceInfo() {
        ServiceInfoResponse response =
                managementService.getServiceInfo();

        return ResponseEntity.ok(response);
    }
}