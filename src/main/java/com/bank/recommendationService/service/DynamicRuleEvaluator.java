package com.bank.recommendationService.service;

import com.bank.recommendationService.entity.RuleArgumentEntity;
import com.bank.recommendationService.entity.RuleQueryEntity;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DynamicRuleEvaluator {

    private final RecommendationRepository recommendationRepository;

    public DynamicRuleEvaluator(
            RecommendationRepository recommendationRepository
    ) {
        this.recommendationRepository = recommendationRepository;
    }

    public boolean evaluate(UUID userId, RuleQueryEntity ruleQuery) {
        List<String> arguments = ruleQuery.getArguments()
                .stream()
                .map(RuleArgumentEntity::getValue)
                .toList();

        boolean result = switch (ruleQuery.getQuery()) {
            case "USER_OF" ->
                    evaluateUserOf(userId, arguments);

            case "ACTIVE_USER_OF" ->
                    evaluateActiveUserOf(userId, arguments);

            case "TRANSACTION_SUM_COMPARE" ->
                    evaluateTransactionSumCompare(userId, arguments);

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" ->
                    evaluateDepositWithdrawCompare(userId, arguments);

            default -> throw new IllegalArgumentException(
                    "Неизвестный тип запроса: " + ruleQuery.getQuery()
            );
        };

        return ruleQuery.isNegate() ? !result : result;
    }

    private boolean evaluateUserOf(
            UUID userId,
            List<String> arguments
    ) {
        requireArguments(arguments, 1, "USER_OF");

        String productType = arguments.get(0);

        return recommendationRepository.hasProductType(
                userId,
                productType
        );
    }

    private boolean evaluateActiveUserOf(
            UUID userId,
            List<String> arguments
    ) {
        requireArguments(arguments, 1, "ACTIVE_USER_OF");

        String productType = arguments.get(0);

        long transactionCount =
                recommendationRepository.getTransactionCount(
                        userId,
                        productType
                );

        return transactionCount >= 5;
    }

    private boolean evaluateTransactionSumCompare(
            UUID userId,
            List<String> arguments
    ) {
        requireArguments(
                arguments,
                4,
                "TRANSACTION_SUM_COMPARE"
        );

        String productType = arguments.get(0);
        String transactionType = arguments.get(1);
        String operator = arguments.get(2);
        long constant = parseNonNegativeLong(arguments.get(3));

        long transactionAmount =
                recommendationRepository.getTransactionAmount(
                        userId,
                        productType,
                        transactionType
                );

        return compare(
                transactionAmount,
                constant,
                operator
        );
    }

    private boolean evaluateDepositWithdrawCompare(
            UUID userId,
            List<String> arguments
    ) {
        requireArguments(
                arguments,
                2,
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW"
        );

        String productType = arguments.get(0);
        String operator = arguments.get(1);

        long depositAmount =
                recommendationRepository.getTransactionAmount(
                        userId,
                        productType,
                        "DEPOSIT"
                );

        long withdrawAmount =
                recommendationRepository.getTransactionAmount(
                        userId,
                        productType,
                        "WITHDRAW"
                );

        return compare(
                depositAmount,
                withdrawAmount,
                operator
        );
    }

    private boolean compare(
            long left,
            long right,
            String operator
    ) {
        return switch (operator) {
            case ">" -> left > right;
            case "<" -> left < right;
            case "=" -> left == right;
            case ">=" -> left >= right;
            case "<=" -> left <= right;

            default -> throw new IllegalArgumentException(
                    "Неизвестный оператор сравнения: " + operator
            );
        };
    }

    private void requireArguments(
            List<String> arguments,
            int expectedCount,
            String queryName
    ) {
        if (arguments == null || arguments.size() != expectedCount) {
            int actualCount =
                    arguments == null ? 0 : arguments.size();

            throw new IllegalArgumentException(
                    "Запрос " + queryName
                            + " должен содержать аргументов: "
                            + expectedCount
                            + ". Получено: "
                            + actualCount
            );
        }
    }

    private long parseNonNegativeLong(String value) {
        try {
            long number = Long.parseLong(value);

            if (number < 0) {
                throw new IllegalArgumentException(
                        "Число не может быть отрицательным: " + value
                );
            }

            return number;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Некорректное число: " + value,
                    exception
            );
        }
    }
}
