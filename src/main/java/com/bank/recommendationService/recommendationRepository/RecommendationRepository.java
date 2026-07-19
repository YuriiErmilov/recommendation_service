package com.bank.recommendationService.recommendationRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;


    public RecommendationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasProductType(UUID userId, String productType) {
        Long count = jdbcTemplate.queryForObject(
                """
                    SELECT COUNT(*)
                    FROM TRANSACTIONS t
                    JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                    WHERE t.USER_ID = ?
                    AND p.TYPE = ?
                    """,
                Long.class,
                userId,
                productType
        );
        return count != null && count > 0;
    }
    public long getTransactionAmount(UUID userId, String productType, String transactionType) {
     Long amount = jdbcTemplate.queryForObject(
             """
                     SELECT COALESCE(SUM(t.AMOUNT),0)
                     FROM TRANSACTIONS t
                     JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                     WHERE t.USER_ID = ?
                     AND p.TYPE = ?
                     AND t.TYPE = ? 
                     """,
             Long.class,
             userId,
             productType,
             transactionType
     );
     return amount == null ? 0 : amount;
    }
}
