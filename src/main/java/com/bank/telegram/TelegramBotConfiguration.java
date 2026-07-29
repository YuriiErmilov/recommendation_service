package com.bank.telegram;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public TelegramBot telegramBot(
            @Value("${telegram.bot.token}") String token
    ) {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "Не указан токен Telegram-бота. " +
                            "Добавьте переменную окружения TELEGRAM_BOT_TOKEN"
            );
        }

        return new TelegramBot(token);
    }
}