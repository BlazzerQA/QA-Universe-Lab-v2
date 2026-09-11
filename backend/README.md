# QA Universe Lab — Backend

REST API сервер на Spring Boot для проекта QA Universe Lab.

## Стек

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA + SQLite
- Spring Boot Actuator
- Spring Security Crypto (BCrypt)

## Запуск

```bash
mvn spring-boot:run
```

Бэкенд будет доступен по адресу `http://localhost:8080`.

## API Endpoints

- `POST /api/auth/register` — регистрация
- `POST /api/auth/login` — вход (возвращает `token`)
- `GET /api/auth/profile` — профиль пользователя (требует `Authorization: Bearer <token>`)
- `PUT /api/auth/profile` — обновление имени (требует `Authorization: Bearer <token>`)
- `GET /api/products` — список продуктов
- `POST /api/products` — добавить продукт
- `DELETE /api/products/{id}` — удалить продукт
- `GET /api/collections/list` — коллекция логов
- `GET /api/collections/set` — множество пользователей
- `GET /api/collections/map` — конфигурация
- `GET /api/dictionary/translate?word=...` — перевод слова
- `GET /actuator/health` — health check

## CORS

Для разработки настроен `@CrossOrigin(origins = "http://localhost:5173")` — запросы с фронтенда на Vite проксируются без проблем.
