package com.bank.recommendationService.controller;

import com.bank.recommendationService.dto.DynamicRuleListResponse;
import com.bank.recommendationService.dto.DynamicRuleRequest;
import com.bank.recommendationService.dto.DynamicRuleResponse;
import com.bank.recommendationService.dto.RuleStatisticsResponse;
import com.bank.recommendationService.service.DynamicRuleService;
import com.bank.recommendationService.service.RuleStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    private final RuleStatisticsService ruleStatisticsService;

    public DynamicRuleController(
            DynamicRuleService dynamicRuleService,
            RuleStatisticsService ruleStatisticsService
    ) {
        this.dynamicRuleService =
                dynamicRuleService;

        this.ruleStatisticsService =
                ruleStatisticsService;
    }

    @PostMapping
    public ResponseEntity<DynamicRuleResponse> createRule(
            @RequestBody DynamicRuleRequest request
    ) {
        DynamicRuleResponse response =
                dynamicRuleService.createRule(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<DynamicRuleListResponse> getAllRules() {
        DynamicRuleListResponse response =
                new DynamicRuleListResponse(
                        dynamicRuleService.getAllRules()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<RuleStatisticsResponse> getStatistics() {
        RuleStatisticsResponse response =
                new RuleStatisticsResponse(
                        ruleStatisticsService.getAllStatistics()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(
            @PathVariable Long id
    ) {
        dynamicRuleService.deleteRule(id);

        return ResponseEntity.noContent().build();
    }
}
