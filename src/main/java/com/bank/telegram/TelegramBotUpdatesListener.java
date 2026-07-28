package com.bank.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger =
            LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TelegramRecommendationService recommendationService;

    public TelegramBotUpdatesListener(
            TelegramBot telegramBot,
            TelegramRecommendationService recommendationService
    ) {
        this.telegramBot = telegramBot;
        this.recommendationService = recommendationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this, exception -> {
            logger.error(
                    "Ошибка при получении обновлений Telegram",
                    exception
            );
        });

        logger.info("Telegram-бот запущен");
    }

    @PreDestroy
    public void destroy() {
        telegramBot.removeGetUpdatesListener();
        logger.info("Telegram-бот остановлен");
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            try {
                processUpdate(update);
            } catch (Exception exception) {
                logger.error(
                        "Ошибка обработки Telegram update: {}",
                        update.updateId(),
                        exception
                );
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        Message message = update.message();

        if (message == null || message.text() == null) {
            return;
        }

        Long chatId = message.chat().id();
        String text = message.text().trim();

        String answer = handleCommand(text);

        telegramBot.execute(
                new SendMessage(chatId, answer)
        );
    }

    private String handleCommand(String text) {
        if (text.equals("/start")) {
            return getWelcomeMessage();
        }

        if (text.equals("/help")) {
            return getHelpMessage();
        }

        if (text.equals("/recommend")) {
            return """
                    Не указан username.

                    Используйте команду:
                    /recommend username

                    Пример:
                    /recommend ivan
                    """;
        }

        if (text.startsWith("/recommend ")) {
            String username = text
                    .substring("/recommend ".length())
                    .trim();

            if (username.startsWith("@")) {
                username = username.substring(1);
            }

            if (username.isBlank()) {
                return """
                        Не указан username.

                        Используйте команду:
                        /recommend username
                        """;
            }

            return recommendationService
                    .getRecommendationsByUsername(username);
        }

        return """
                Неизвестная команда.

                Для просмотра доступных команд используйте:
                /help
                """;
    }

    private String getWelcomeMessage() {
        return """
                Привет!

                Я бот сервиса банковских рекомендаций.

                Я помогу получить список банковских продуктов,
                подходящих конкретному пользователю.

                Используйте команду:
                /recommend username

                Для просмотра справки:
                /help
                """;
    }

    private String getHelpMessage() {
        return """
                Доступные команды:

                /start — показать приветствие

                /help — показать справку

                /recommend username —
                получить рекомендации для пользователя

                Пример:
                /recommend ivan
                """;
    }
}
