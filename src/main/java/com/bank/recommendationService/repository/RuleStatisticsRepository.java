package com.bank.recommendationService.repository;

import com.bank.recommendationService.entity.RuleStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Репозиторий для хранения и изменения статистики
 * срабатывания динамических правил.
 */
public interface RuleStatisticsRepository
        extends JpaRepository<RuleStatisticsEntity, Long> {

    /**
     * Находит статистику по идентификатору динамического правила.
     *
     * @param ruleId идентификатор динамического правила
     * @return статистика правила или пустой Optional
     */
    Optional<RuleStatisticsEntity> findByRuleId(Long ruleId);

    /**
     * Атомарно увеличивает счётчик срабатывания правила на единицу.
     *
     * @param ruleId идентификатор динамического правила
     * @return количество изменённых строк
     */
    @Modifying
    @Query("""
            UPDATE RuleStatisticsEntity statistics
            SET statistics.count = statistics.count + 1
            WHERE statistics.ruleId = :ruleId
            """)
    int incrementCount(
            @Param("ruleId") Long ruleId
    );

    /**
     * Удаляет статистику по идентификатору динамического правила.
     *
     * @param ruleId идентификатор динамического правила
     */
}
