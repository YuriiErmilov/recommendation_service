package com.bank.recommendationService.rule;

import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {
    private static final UUID RECOMMENDATION_ID = UUID. fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f") ;
    private static final String RECOMMENDATION_NAME = "Простой кредит";
    private static final String RECOMMENDATION_TEXT =
            """
            Откройте мир выгодных кредитов с нами!
                    
            Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.
                    
            Почему выбирают нас:
                    
            Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.
                    
            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.
                    
            Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.
                    
            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!
            """;

    private final RecommendationRepository  recommendationRepository;

    public SimpleCreditRuleSet(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<Recommendation> check(UUID userId) {
        boolean hasCredit = recommendationRepository.hasProductType(userId, "CREDIT");
        long debitDeposit = recommendationRepository.getTransactionAmount(
                userId,
                "DEBIT",
                "DEPOSIT"
        );
        long debitWithdrawals =  recommendationRepository.getTransactionAmount(
                userId,
                "DEBIT",
                "WITHDRAW"
        );

        boolean depositGreaterWithdrawals = debitDeposit > debitWithdrawals;

        boolean withdrawalsGreaterThanLimit = debitWithdrawals > 100_000;

        boolean recommendationMatches = !hasCredit && depositGreaterWithdrawals && withdrawalsGreaterThanLimit;

        if (!recommendationMatches) {
            return Optional.empty();
        }
        Recommendation recommendation = new Recommendation(
                RECOMMENDATION_ID,
                RECOMMENDATION_NAME,
                RECOMMENDATION_TEXT
        );
        return   Optional.of(recommendation);
    }


}
