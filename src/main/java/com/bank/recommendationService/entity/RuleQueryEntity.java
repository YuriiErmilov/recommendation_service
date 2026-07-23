package com.bank.recommendationService.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rule_query" )
public class RuleQueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_name", nullable = false)
    private String query;

    @Column(name = "negate", nullable = false)
    private boolean negate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dynamic_rule_id", nullable = false)
    private DynamicRuleEntity dynamicRule;

    @OneToMany(
            mappedBy = "ruleQuery",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("orderIndex ASC")

    private List<RuleArgumentEntity> arguments = new ArrayList<>();

    public RuleQueryEntity() {
    }

    public RuleQueryEntity(String query, boolean negate) {
        this.query = query;
        this.negate = negate;
    }

    public void addArgument(RuleArgumentEntity argument) {
        this.arguments.add(argument);
        argument.setRuleQuery(this);
    }

    public void removeArgument(RuleArgumentEntity argument) {
        this.arguments.remove(argument);
        argument.setRuleQuery(null);
    }
    public Long getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public boolean isNegate() {
        return negate;
    }

    public DynamicRuleEntity getDynamicRule() {
        return dynamicRule;
    }

    public List<RuleArgumentEntity> getArguments() {
        return arguments;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public void setDynamicRule(DynamicRuleEntity dynamicRule) {
        this.dynamicRule = dynamicRule;
    }

}
