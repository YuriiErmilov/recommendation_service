package com.bank.telegram;

import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import com.bank.recommendationService.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис получения и форматирования рекомендаций
 * для отправки пользователю Telegram.
 */
@Service
public class TelegramRecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final RecommendationService recommendationService;

    public TelegramRecommendationService(
            RecommendationRepository recommendationRepository,
            RecommendationService recommendationService
    ) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationService = recommendationService;
    }

    /**
     * Возвращает текстовое представление рекомендаций
     * для пользователя, найденного по username.
     *
     * @param username имя пользователя в базе знаний
     * @return сообщение для отправки в Telegram
     */
    public String getRecommendationsByUsername(String username) {
        Optional<UUID> userId =
                recommendationRepository.findUserIdByUsername(username);

        if (userId.isEmpty()) {
            return "Пользователь @" + username + " не найден.";
        }

        List<Recommendation> recommendations =
                recommendationService.getRecommendation(userId.get());

        if (recommendations.isEmpty()) {
            return "Для пользователя @" + username
                    + " подходящих рекомендаций нет.";
        }

        return formatRecommendations(
                username,
                recommendations
        );
    }

    private String formatRecommendations(
            String username,
            List<Recommendation> recommendations
    ) {
        StringBuilder result = new StringBuilder();

        result.append("Рекомендации для @")
                .append(username)
                .append(":\n\n");

        for (int i = 0; i < recommendations.size(); i++) {
            Recommendation recommendation =
                    recommendations.get(i);

            result.append(i + 1)
                    .append(". ")
                    .append(recommendation.name())
                    .append("\n")
                    .append(recommendation.text())
                    .append("\n\n");
        }

        return result.toString().trim();
    }
}