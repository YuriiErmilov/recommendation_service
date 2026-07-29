package com.bank.recommendationService.recommendationRepository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для обращения к базе знаний H2.
 * <p>
 * Выполняет запросы к пользователям, продуктам и транзакциям.
 * Результаты основных запросов кешируются с помощью Caffeine.
 */

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Cache<ProductKey, Boolean> productTypeCache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .expireAfterWrite(Duration.ofMinutes(30))
                    .build();

    private final Cache<ProductKey, Long> transactionCountCache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .expireAfterWrite(Duration.ofMinutes(30))
                    .build();

    private final Cache<TransactionAmountKey, Long> transactionAmountCache =
            Caffeine.newBuilder()
                    .maximumSize(20_000)
                    .expireAfterWrite(Duration.ofMinutes(30))
                    .build();

    public RecommendationRepository(
            @Qualifier("knowledgeJdbcTemplate")
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверяет наличие продукта указанного типа у пользователя.
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return true, если пользователь использует продукт
     */

    public boolean hasProductType(UUID userId, String productType) {
        ProductKey key = new ProductKey(userId, productType);

        return productTypeCache.get(
                key,
                currentKey -> loadHasProductType(
                        currentKey.userId(),
                        currentKey.productType()
                )
        );
    }

    /**
     * Возвращает количество транзакций пользователя по типу продукта.
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return количество транзакций
     */

    public long getTransactionCount(UUID userId, String productType) {
        ProductKey key = new ProductKey(userId, productType);

        return transactionCountCache.get(
                key,
                currentKey -> loadTransactionCount(
                        currentKey.userId(),
                        currentKey.productType()
                )
        );
    }

    /**
     * Возвращает общую сумму транзакций указанного типа.
     *
     * @param userId          идентификатор пользователя
     * @param productType     тип банковского продукта
     * @param transactionType тип транзакции
     * @return сумма транзакций
     */

    public long getTransactionAmount(
            UUID userId,
            String productType,
            String transactionType
    ) {
        TransactionAmountKey key = new TransactionAmountKey(
                userId,
                productType,
                transactionType
        );

        return transactionAmountCache.get(
                key,
                currentKey -> loadTransactionAmount(
                        currentKey.userId(),
                        currentKey.productType(),
                        currentKey.transactionType()
                )
        );
    }

    private boolean loadHasProductType(UUID userId, String productType) {
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

    private long loadTransactionCount(UUID userId, String productType) {
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

        return count == null ? 0 : count;
    }

    private long loadTransactionAmount(
            UUID userId,
            String productType,
            String transactionType
    ) {
        Long amount = jdbcTemplate.queryForObject(
                """
                        SELECT COALESCE(SUM(t.AMOUNT), 0)
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

    /**
     * Очищает кеш наличия продуктов, количества транзакций
     * и сумм транзакций.
     */

    public void clearCaches() {
        productTypeCache.invalidateAll();
        transactionCountCache.invalidateAll();
        transactionAmountCache.invalidateAll();
    }

    /**
     * Находит идентификатор пользователя по username.
     * <p>
     * Поиск выполняется без учёта регистра.
     *
     * @param username имя пользователя
     * @return идентификатор пользователя или пустой Optional
     */

    public Optional<UUID> findUserIdByUsername(String username) {
        String sql = """
                SELECT id
                FROM users
                WHERE LOWER(username) = LOWER(?)
                """;

        List<UUID> userIds = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        resultSet.getObject("id", UUID.class),
                username
        );

        return userIds.stream().findFirst();
    }

    private record ProductKey(
            UUID userId,
            String productType
    ) {
    }

    private record TransactionAmountKey(
            UUID userId,
            String productType,
            String transactionType
    ) {
    }

}
