package com.bank.recommendationService.repository;

import com.bank.recommendationService.entity.RuleStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RuleStatisticsRepository
        extends JpaRepository<RuleStatisticsEntity, Long> {

    Optional<RuleStatisticsEntity> findByRuleId(Long ruleId);

    @Modifying
    @Query("""
            UPDATE RuleStatisticsEntity statistics
            SET statistics.count = statistics.count + 1
            WHERE statistics.ruleId = :ruleId
            """)
    int incrementCount(
            @Param("ruleId") Long ruleId
    );

    void deleteByRuleId(Long ruleId);
}
