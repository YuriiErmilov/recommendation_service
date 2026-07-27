package com.bank.recommendationService.service;

import com.bank.recommendationService.dto.DynamicRuleRequest;
import com.bank.recommendationService.dto.DynamicRuleResponse;
import com.bank.recommendationService.dto.RuleQueryDto;
import com.bank.recommendationService.entity.DynamicRuleEntity;
import com.bank.recommendationService.entity.RuleArgumentEntity;
import com.bank.recommendationService.entity.RuleQueryEntity;
import com.bank.recommendationService.repository.DynamicRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final RuleStatisticsService ruleStatisticsService;

    public DynamicRuleService(
            DynamicRuleRepository dynamicRuleRepository,
            RuleStatisticsService ruleStatisticsService
    ) {
        this.dynamicRuleRepository =
                dynamicRuleRepository;

        this.ruleStatisticsService =
                ruleStatisticsService;
    }

    @Transactional
    public DynamicRuleResponse createRule(
            DynamicRuleRequest request
    ) {
        DynamicRuleEntity dynamicRule =
                new DynamicRuleEntity();

        dynamicRule.setProductName(
                request.getProductName()
        );

        dynamicRule.setProductId(
                request.getProductId()
        );

        dynamicRule.setProductText(
                request.getProductText()
        );

        for (RuleQueryDto queryDto : request.getRule()) {
            RuleQueryEntity ruleQuery =
                    new RuleQueryEntity();

            ruleQuery.setQuery(
                    queryDto.getQuery()
            );

            ruleQuery.setNegate(
                    queryDto.isNegate()
            );

            List<String> arguments =
                    queryDto.getArguments();

            for (int i = 0; i < arguments.size(); i++) {
                RuleArgumentEntity argument =
                        new RuleArgumentEntity();

                argument.setValue(
                        arguments.get(i)
                );

                argument.setOrderIndex(i);

                ruleQuery.addArgument(argument);
            }

            dynamicRule.addRuleQuery(ruleQuery);
        }

        DynamicRuleEntity savedRule =
                dynamicRuleRepository.save(dynamicRule);

        ruleStatisticsService.createStatistics(
                savedRule.getId()
        );

        return convertToResponse(savedRule);
    }

    @Transactional(readOnly = true)
    public List<DynamicRuleResponse> getAllRules() {
        return dynamicRuleRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public void deleteRule(Long id) {
        if (!dynamicRuleRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Динамическое правило с id "
                            + id
                            + " не найдено"
            );
        }

        dynamicRuleRepository.deleteById(id);
    }

    private DynamicRuleResponse convertToResponse(
            DynamicRuleEntity entity
    ) {
        List<RuleQueryDto> queries =
                new ArrayList<>();

        for (RuleQueryEntity queryEntity :
                entity.getRuleQueries()) {

            List<String> arguments =
                    queryEntity.getArguments()
                            .stream()
                            .map(RuleArgumentEntity::getValue)
                            .toList();

            RuleQueryDto queryDto =
                    new RuleQueryDto(
                            queryEntity.getQuery(),
                            arguments,
                            queryEntity.isNegate()
                    );

            queries.add(queryDto);
        }

        return new DynamicRuleResponse(
                entity.getId(),
                entity.getProductName(),
                entity.getProductId(),
                entity.getProductText(),
                queries
        );
    }
}