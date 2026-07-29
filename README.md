# Recommendation Service

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)
![Maven](https://img.shields.io/badge/Maven-3-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![H2](https://img.shields.io/badge/H2-Database-lightgrey)

Recommendation Service — сервис персональных банковских рекомендаций.

Приложение анализирует продукты и транзакции пользователя, проверяет статические и динамические правила и возвращает список подходящих банковских продуктов через REST API или Telegram-бота.

## Возможности

### Для пользователя

- получение рекомендаций через REST API;
- получение рекомендаций через Telegram-бота;
- просмотр справки Telegram-бота.

### Для менеджера

- создание динамических правил;
- получение списка динамических правил;
- удаление динамических правил;
- получение статистики срабатывания правил.

### Для внешней системы

- очистка кешей приложения;
- получение названия и версии запущенного сервиса.

## Стек технологий

- Java 17
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Spring JDBC
- PostgreSQL
- H2 Database
- Liquibase
- Caffeine Cache
- Telegram Bot API
- OpenAPI / Swagger
- Maven
- GitHub Projects
- GitHub Wiki

## Архитектура

Приложение построено по многослойной архитектуре.

```
REST Controller
        │
        ▼
Service Layer
        │
        ▼
Repositories
        │
 ┌──────┴─────────┐
 ▼                ▼
PostgreSQL        H2
(правила)      (база знаний)
```

- REST API используется внешними клиентами.
- Telegram Bot предоставляет альтернативный интерфейс получения рекомендаций.
- PostgreSQL хранит динамические правила.
- H2 используется как база знаний с информацией о клиентах и продуктах.

Приложение использует две базы данных:

- **PostgreSQL** — хранение динамических правил и статистики их срабатываний;
- **H2** — база знаний с пользователями, банковскими продуктами и транзакциями.

При формировании рекомендаций сервис:

1. проверяет статические правила;
2. загружает динамические правила из PostgreSQL;
3. проверяет условия динамических правил по данным H2;
4. увеличивает статистику сработавших динамических правил;
5. объединяет результаты;
6. удаляет дублирующиеся рекомендации;
7. возвращает итоговый список.


## Структура проекта

```
src
 ├── controller
 ├── service
 ├── repository
 ├── model
 ├── dto
 ├── telegram
 ├── cache
 ├── configuration
 └── resources
```

Основные компоненты проекта:

- Controller — REST API;
- Service — бизнес-логика;
- Repository — работа с БД;
- DTO — модели запросов и ответов;
- Telegram — Telegram Bot;
- Cache — кэширование рекомендаций;
- Liquibase — миграции PostgreSQL.

## REST API

Основные REST-endpoint:

| Метод | Путь | Назначение |
|---|---|---|
| `GET` | `/recommendation/{userId}` | Получение рекомендаций |
| `POST` | `/rule` | Создание динамического правила |
| `GET` | `/rule` | Получение списка правил |
| `DELETE` | `/rule/{id}` | Удаление правила |
| `GET` | `/rule/stats` | Получение статистики |
| `POST` | `/management/clear-caches` | Очистка кешей |
| `GET` | `/management/info` | Информация о сервисе |

После запуска Swagger UI доступен по адресу:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Telegram-бот

Поддерживаемые команды:

```text
/start
/help
/recommend username
```

Пример:

```text
/recommend larry.bruen
```

## Требования для запуска

- Java 17;
- PostgreSQL;
- файл базы знаний H2;
- токен Telegram-бота.

## Переменные окружения

| Переменная | Назначение |
|---|---|
| `TELEGRAM_BOT_TOKEN` | токен Telegram-бота |
| `SPRING_DATASOURCE_URL` | URL PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | имя пользователя PostgreSQL |
| `SPRING_DATASOURCE_PASSWORD` | пароль PostgreSQL |
| `KNOWLEDGE_DATASOURCE_URL` | путь к базе знаний H2 |
| `KNOWLEDGE_DATASOURCE_USERNAME` | имя пользователя H2, если используется |
| `KNOWLEDGE_DATASOURCE_PASSWORD` | пароль H2, если используется |
| `SERVER_PORT` | порт приложения, по умолчанию `8080` |

## Сборка

### Windows

```bash
mvnw.cmd clean package
```

### Linux или macOS

```bash
./mvnw clean package
```

## Запуск

```bash
java -jar target/recommendationService-0.0.1-SNAPSHOT.jar
```

Перед запуском необходимо настроить PostgreSQL, указать путь к H2 и добавить требуемые переменные окружения.

## Документация

- [Главная страница Wiki](https://github.com/YuriiErmilov/recommendation_service/wiki)
- [Требования проекта](https://github.com/YuriiErmilov/recommendation_service/wiki/Requirements)
- [Архитектура](https://github.com/YuriiErmilov/recommendation_service/wiki/Architecture)
- [REST API](https://github.com/YuriiErmilov/recommendation_service/wiki/REST-API)
- [Инструкция по развёртыванию](https://github.com/YuriiErmilov/recommendation_service/wiki/Deployment)
- [Матрица трассировки](https://github.com/YuriiErmilov/recommendation_service/wiki/Traceability-Matrix)

## Репозиторий

https://github.com/YuriiErmilov/recommendation_service

## Автор

Разработчик: Yurii Ermilov

Курсовой проект по Spring Boot.

