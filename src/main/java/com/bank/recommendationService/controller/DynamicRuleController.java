package com.bank.recommendationService.controller;

import com.bank.recommendationService.dto.DynamicRuleListResponse;
import com.bank.recommendationService.dto.DynamicRuleRequest;
import com.bank.recommendationService.dto.DynamicRuleResponse;
import com.bank.recommendationService.dto.RuleStatisticsResponse;
import com.bank.recommendationService.service.DynamicRuleService;
import com.bank.recommendationService.service.RuleStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST-контроллер управления динамическими правилами.
 */
@RestController
@RequestMapping("/rule")
@Tag(
        name = "Динамические правила",
        description = """
                Создание, получение, удаление динамических правил
                и просмотр статистики их срабатываний
                """
)
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;
    private final RuleStatisticsService ruleStatisticsService;

    public DynamicRuleController(
            DynamicRuleService dynamicRuleService,
            RuleStatisticsService ruleStatisticsService
    ) {
        this.dynamicRuleService = dynamicRuleService;
        this.ruleStatisticsService = ruleStatisticsService;
    }

    /**
     * Создаёт динамическое правило.
     *
     * @param request описание продукта и условий
     * @return созданное правило
     */
    @Operation(
            summary = "Создать динамическое правило",
            description = """
                Сохраняет динамическое правило в PostgreSQL
                и создаёт для него статистику со значением 0.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Правило создано",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = DynamicRuleResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректное тело запроса",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<DynamicRuleResponse> createRule(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Данные продукта и список условий правила",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = DynamicRuleRequest.class
                            ),
                            examples = @ExampleObject(
                                    name = "Правило для активного пользователя DEBIT",
                                    summary = "Пример динамического правила",
                                    value = """
                                        {
                                          "product_name": "Premium Debit Card",
                                          "product_id": "11111111-1111-1111-1111-111111111111",
                                          "product_text": "Оформите премиальную дебетовую карту.",
                                          "rule": [
                                            {
                                              "query": "USER_OF",
                                              "arguments": [
                                                "DEBIT"
                                              ],
                                              "negate": false
                                            },
                                            {
                                              "query": "ACTIVE_USER_OF",
                                              "arguments": [
                                                "DEBIT"
                                              ],
                                              "negate": false
                                            }
                                          ]
                                        }
                                        """
                            )
                    )
            )
            @RequestBody DynamicRuleRequest request
    ) {
        DynamicRuleResponse response =
                dynamicRuleService.createRule(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Возвращает все динамические правила.
     *
     * @return список правил
     */
    @Operation(
            summary = "Получить все динамические правила"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список правил получен",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation =
                                    DynamicRuleListResponse.class
                    )
            )
    )
    @GetMapping
    public ResponseEntity<DynamicRuleListResponse> getAllRules() {
        DynamicRuleListResponse response =
                new DynamicRuleListResponse(
                        dynamicRuleService.getAllRules()
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Возвращает статистику динамических правил.
     *
     * @return статистика срабатываний
     */
    @Operation(
            summary = "Получить статистику правил",
            description = """
                Возвращает идентификатор каждого правила
                и количество его успешных срабатываний.
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Статистика получена",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = RuleStatisticsResponse.class
                    ),
                    examples = @ExampleObject(
                            value = """
                                {
                                  "stats": [
                                    {
                                      "rule_id": 5,
                                      "count": 17
                                    },
                                    {
                                      "rule_id": 6,
                                      "count": 4
                                    }
                                  ]
                                }
                                """
                    )
            )
    )
    @GetMapping("/stats")
    public ResponseEntity<RuleStatisticsResponse> getStatistics() {
        RuleStatisticsResponse response =
                new RuleStatisticsResponse(
                        ruleStatisticsService.getAllStatistics()
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Удаляет динамическое правило.
     *
     * @param id идентификатор правила
     * @return ответ без тела
     */
    @Operation(
            summary = "Удалить динамическое правило",
            description = """
                    Удаляет правило и связанную с ним статистику.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Правило удалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Правило не найдено",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(
            @Parameter(
                    description = "Внутренний идентификатор правила",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        dynamicRuleService.deleteRule(id);

        return ResponseEntity.noContent().build();
    }
}
