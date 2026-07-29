package com.bank.recommendationService.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "rule_argument")
public class RuleArgumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "argument_value", nullable = false)
    private String value;

    @Column(name = "argument_order", nullable = false)
    private Integer orderIndex;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_query_id", nullable = false)
    private RuleQueryEntity ruleQuery;

    public RuleArgumentEntity() {
    }

    public RuleArgumentEntity(String value, int orderIndex) {
        this.value = value;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public RuleQueryEntity getRuleQuery() {
        return ruleQuery;
    }

    public void setRuleQuery(RuleQueryEntity ruleQuery) {
        this.ruleQuery = ruleQuery;
    }


}
