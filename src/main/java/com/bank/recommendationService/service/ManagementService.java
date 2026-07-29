package com.bank.recommendationService.service;

import com.bank.recommendationService.dto.ServiceInfoResponse;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

/**
 * Сервис технического управления приложением.
 * <p>
 * Позволяет очищать кеши базы знаний и получать
 * информацию о текущей сборке приложения.
 */

@Service
public class ManagementService {

    private final RecommendationRepository recommendationRepository;
    private final BuildProperties buildProperties;

    public ManagementService(
            RecommendationRepository recommendationRepository,
            BuildProperties buildProperties
    ) {
        this.recommendationRepository =
                recommendationRepository;

        this.buildProperties =
                buildProperties;
    }

    /**
     * Очищает все кеши репозитория рекомендаций.
     */

    public void clearCaches() {
        recommendationRepository.clearCaches();
    }

    /**
     * Возвращает название и версию текущей сборки приложения.
     *
     * @return информация о сервисе
     */

    public ServiceInfoResponse getServiceInfo() {
        return new ServiceInfoResponse(
                buildProperties.getName(),
                buildProperties.getVersion()
        );
    }
}