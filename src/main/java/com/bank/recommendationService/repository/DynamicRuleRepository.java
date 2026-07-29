package com.bank.recommendationService.repository;

import com.bank.recommendationService.entity.DynamicRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для управления динамическими правилами рекомендаций
 * в PostgreSQL.
 */
public interface DynamicRuleRepository
        extends JpaRepository<DynamicRuleEntity, Long> {
}
