package com.bank.recommendationService.controller;

import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.dto.RecommendationResponse;
import com.bank.recommendationService.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST-контроллер получения банковских рекомендаций.
 */
@RestController
@RequestMapping("/recommendation")
@Tag(
        name = "Рекомендации",
        description = "Получение персональных банковских рекомендаций"
)
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService
    ) {
        this.recommendationService = recommendationService;
    }

    /**
     * Возвращает рекомендации для пользователя.
     *
     * @param userId идентификатор пользователя
     * @return пользователь и список подходящих рекомендаций
     */
    @Operation(
            summary = "Получить рекомендации",
            description = """
                    Проверяет статические и динамические правила
                    и возвращает подходящие банковские продукты.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Рекомендации успешно сформированы",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation =
                                            RecommendationResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный UUID пользователя",
                    content = @Content
            )
    })
    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendation(
            @Parameter(
                    description = "UUID пользователя",
                    required = true,
                    example = "cd515076-5d8a-44be-930e-8d4fcb79f42d"
            )
            @PathVariable UUID userId
    ) {
        List<Recommendation> recommendations =
                recommendationService.getRecommendation(userId);

        return new RecommendationResponse(
                userId,
                recommendations
        );
    }
}