package com.bank.recommendationService.service;

import com.bank.recommendationService.dto.ServiceInfoResponse;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

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

    public void clearCaches() {
        recommendationRepository.clearCaches();
    }

    public ServiceInfoResponse getServiceInfo() {
        return new ServiceInfoResponse(
                buildProperties.getName(),
                buildProperties.getVersion()
        );
    }
}