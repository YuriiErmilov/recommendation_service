package com.bank.recommendationService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rule_statistics")
public class RuleStatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "rule_id",
            nullable = false,
            unique = true
    )
    private Long ruleId;

    @Column(
            name = "count",
            nullable = false
    )
    private Long count;

    public RuleStatisticsEntity() {
    }

    public RuleStatisticsEntity(
            Long ruleId,
            Long count
    ) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public Long getCount() {
        return count;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
