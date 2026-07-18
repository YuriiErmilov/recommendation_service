package com.bank.recommendationService.rule;

import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import com.bank.recommendationService.service.RecommendationService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private static final UUID RECOMMENDATION_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");

    private static final String RECOMMENDATION_NAME = "Invest 500";

    private static final String RECOMMENDATION_TEXT =
            """
             Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! \\
             Воспользуйтесь налоговыми льготами и начните инвестировать с умом. \\
             Пополните счет до конца года и получите выгоду в виде вычета на взнос \\
             в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, \\
             снизить риски и следить за актуальными рыночными тенденциями. \\
             Откройте ИИС сегодня и станьте ближе к финансовой независимости!    
                    """;

    private final RecommendationRepository  recommendationRepository;

    public Invest500RuleSet(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override

    public Optional<Recommendation> check(UUID userId) {
        boolean hasDebit = recommendationRepository.hasProductType(userId, "DEBIT");
        boolean hasInvest = recommendationRepository.hasProductType(userId, "INVEST");

        long savingDeposits = recommendationRepository.getTransactionAmount(
                userId,
                "SEVING",
                "DEPOSIT"
        );
        boolean recommendationMatches = hasDebit && !hasInvest && savingDeposits > 1000;
        if (!recommendationMatches) {
            return Optional.empty();
        }
        Recommendation recommendation = new Recommendation(
                RECOMMENDATION_ID,
                RECOMMENDATION_NAME,
                RECOMMENDATION_TEXT);
        return Optional.of(recommendation);
       }

}
