package com.bank.recommendationService.service;

import com.bank.recommendationService.dto.RuleStatisticsDto;
import com.bank.recommendationService.entity.RuleStatisticsEntity;
import com.bank.recommendationService.repository.RuleStatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис управления статистикой срабатывания динамических правил.
 */

@Service
public class RuleStatisticsService {

    private final RuleStatisticsRepository ruleStatisticsRepository;

    public RuleStatisticsService(
            RuleStatisticsRepository ruleStatisticsRepository
    ) {
        this.ruleStatisticsRepository =
                ruleStatisticsRepository;
    }

    /**
     * Создаёт статистику для нового динамического правила.
     * <p>
     * Если статистика уже существует, новая запись не создаётся.
     *
     * @param ruleId идентификатор динамического правила
     */

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

    /**
     * Атомарно увеличивает счётчик срабатывания правила.
     * <p>
     * Если статистика для правила ещё не существует,
     * создаётся новая запись со значением 1.
     *
     * @param ruleId идентификатор сработавшего правила
     */

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

    /**
     * Возвращает статистику всех динамических правил.
     *
     * @return список идентификаторов правил и количества их срабатываний
     */

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
