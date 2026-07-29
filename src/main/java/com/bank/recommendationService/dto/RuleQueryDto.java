package com.bank.recommendationService.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Одно условие динамического правила")
public class RuleQueryDto {

    @Schema(
            description = """
                Тип проверки динамического правила.

                Допустимые значения:
                USER_OF,
                ACTIVE_USER_OF,
                TRANSACTION_SUM_COMPARE,
                TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
                """,
            example = "USER_OF",
            allowableValues = {
                    "USER_OF",
                    "ACTIVE_USER_OF",
                    "TRANSACTION_SUM_COMPARE",
                    "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW"
            }
    )
    private String query;

    @ArraySchema(
            schema = @Schema(
                    description = "Один аргумент условия",
                    example = "DEBIT"
            ),
            arraySchema = @Schema(
                    description = """
                        Аргументы проверки.

                        USER_OF:
                        [productType]

                        ACTIVE_USER_OF:
                        [productType]

                        TRANSACTION_SUM_COMPARE:
                        [productType, transactionType, operator, amount]

                        TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW:
                        [productType, operator]
                        """
            )
    )
    private List<String> arguments = new ArrayList<>();

    @Schema(
            description = "Инвертировать результат проверки",
            example = "false"
    )
    private boolean negate;

    public RuleQueryDto() {
    }

    public RuleQueryDto(
            String query,
            List<String> arguments,
            boolean negate
    ) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }
}
