package com.bank.recommendationService.rule;

import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final UUID RECOMMENDATION_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");

    private static final String RECOMMENDATION_NAME = "Top Saving";

    private static final String RECOMMENDATION_TEXT =
            """
                    Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!
                    
                    Преимущества «Копилки»:
                    
                    Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.
                    
                    Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.
                    
                    Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.
                    
                    Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!    
                    """;

    private final RecommendationRepository recommendationRepository;

    public TopSavingRuleSet(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<Recommendation> check(UUID userId) {
        boolean hasDebit = recommendationRepository.hasProductType(userId, "DEBIT");
        long debitDeposits = recommendationRepository.getTransactionAmount(
                userId,
                "DEBIT",
                "DEPOSIT"
        );
        long savingDeposit = recommendationRepository.getTransactionAmount(
                userId,
                "SAVING",
                "DEPOSIT"
        );

        long debitWithdrawals = recommendationRepository.getTransactionAmount(
                userId,
                "DEBIT",
                "WITHDRAW"
        );
        boolean enoughDeposit = debitDeposits >= 50000 || savingDeposit >= 50000;
        boolean depositGreaterThanWithdrawals = debitDeposits > debitWithdrawals;
        boolean recommendationMatches = hasDebit && enoughDeposit && depositGreaterThanWithdrawals;

        if (!recommendationMatches) {
            return Optional.empty();
        }
        Recommendation recommendation = new Recommendation(
                RECOMMENDATION_ID,
                RECOMMENDATION_NAME,
                RECOMMENDATION_TEXT
        );
        return Optional.of(recommendation);
    }
}
