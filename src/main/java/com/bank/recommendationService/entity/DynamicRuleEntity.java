package com.bank.recommendationService.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rule")
public class DynamicRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_text", nullable = false, length = 4000)
    private String productText;

    @OneToMany(
            mappedBy = "dynamicRule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("id ASC")
    private List<RuleQueryEntity> ruleQueries = new ArrayList<>();

    public DynamicRuleEntity() {
    }
    public DynamicRuleEntity(String productName, UUID productId, String productText) {
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
    }

    public void addRuleQuery(RuleQueryEntity ruleQuery) {
        ruleQueries.add(ruleQuery);
        ruleQuery.setDynamicRule(this);
    }
    public void removeRuleQuery(RuleQueryEntity ruleQuery) {
        ruleQueries.remove(ruleQuery);
        ruleQuery.setDynamicRule(null);
    }

    public List<RuleQueryEntity> getRuleQueries() {
        return ruleQueries;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

}
