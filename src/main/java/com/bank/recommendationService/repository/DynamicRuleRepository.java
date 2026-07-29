package com.bank.recommendationService.repository;

import com.bank.recommendationService.entity.DynamicRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DynamicRuleRepository extends JpaRepository<DynamicRuleEntity, Long> {
}
