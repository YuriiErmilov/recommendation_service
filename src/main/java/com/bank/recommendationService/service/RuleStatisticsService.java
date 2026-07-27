package com.bank.recommendationService.service;

import com.bank.recommendationService.dto.RuleStatisticsDto;
import com.bank.recommendationService.entity.RuleStatisticsEntity;
import com.bank.recommendationService.repository.RuleStatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RuleStatisticsService {

    private final RuleStatisticsRepository ruleStatisticsRepository;

    public RuleStatisticsService(
            RuleStatisticsRepository ruleStatisticsRepository
    ) {
        this.ruleStatisticsRepository =
                ruleStatisticsRepository;
    }

    @Transactional
    public void createStatistics(Long ruleId) {
        if (ruleStatisticsRepository
                .findByRuleId(ruleId)
                .isPresent()) {
            return;
        }

        RuleStatisticsEntity statistics =
                new RuleStatisticsEntity(
                        ruleId,
                        0L
                );

        ruleStatisticsRepository.save(statistics);
    }

    @Transactional
    public void incrementCount(Long ruleId) {
        int updatedRows =
                ruleStatisticsRepository.incrementCount(ruleId);


        if (updatedRows == 0) {
            RuleStatisticsEntity statistics =
                    new RuleStatisticsEntity(
                            ruleId,
                            1L
                    );

            ruleStatisticsRepository.save(statistics);
        }
    }

    @Transactional(readOnly = true)
    public List<RuleStatisticsDto> getAllStatistics() {
        return ruleStatisticsRepository.findAll()
                .stream()
                .map(statistics ->
                        new RuleStatisticsDto(
                                statistics.getRuleId(),
                                statistics.getCount()
                        )
                )
                .toList();
    }
}
