# QA Universe Lab

Монорепозиторий для практики Middle AQA: Vue 3 SPA + Spring Boot REST API, Docker, knowledge-статьи, API/UI-тесты.

Бренд: **QA Universe Lab**.

## Стек (как в коде)

| Слой | Технологии |
|------|------------|
| Frontend | Vue 3, Vite, Pinia, Vue Router, Axios |
| Backend | Java 21, Spring Boot **4.0.5**, JPA + SQLite, BCrypt (без Spring Security filter chain) |
| Auth | REST login/register/profile + Bearer-токен (in-memory map, проверяется на бэке) |
| Docs API | springdoc-openapi 2.8.6 — Swagger UI |
| Knowledge | Markdown-файлы в `knowledge/`, Flexmark → HTML |
| AQA-тесты | JUnit 5 (unit/slice), RestAssured + TestNG (API), Selenide (UI), Allure |

В коде **нет** Testcontainers, JSON Schema Validation и Spring Boot 3.x.

## Структура

```
QA-Universe-Lab-v2/
├── docker-compose.yml     # единственная точка входа для учеников
├── README.md
├── backend/               # Spring Boot API
│   └── src/test/          # unit / slice (MarkdownService, NoteController, Flexmark)
├── frontend/              # Vue 3 SPA
├── tests/                 # витрина AQA: RestAssured + Selenide
├── knowledge/             # markdown-уроки
├── docs/test-cases/       # ручные тест-кейсы
└── elk/                   # Logstash pipeline (опциональный профиль)
```

## Быстрый старт (Docker)

```bash
git clone <url>
cd QA-Universe-Lab-v2
docker compose up --build
```

| Что | URL |
|-----|-----|
| Фронтенд | http://localhost |
| Бэкенд API | http://localhost:8080 |
| Health | http://localhost:8080/actuator/health |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

Остановка: `docker compose down`.

### Тестовый пользователь

- Телефон: `+79991234567`
- Пароль: `password123`

Создаётся при старте бэкенда, если его ещё нет в SQLite (`qa_lab.db`).

## Модули приложения

- **Auth** — регистрация, логин, профиль (GET/PUT), Bearer
- **Products** — CRUD in-memory + UI `/products-ui`
- **JSON Formatter** — клиентский formatter + tree viewer
- **Roadmap** — план подготовки
- **Collections** — Q&A и задачи (ссылка на https://interview.cups.online/)
- **Mocks** — Q&A по Mockito и задачи (cups.online)
- **Notes / Knowledge** — каталог `knowledge/**/*.md`, API `/api/notes`, UI `/notes`
- **Dictionary** — API `GET /api/dictionary/translate?word=apple` (отдельного UI нет, как в источнике)
- **Swagger** — документация REST

## Локальная разработка без Docker

Терминал 1:

```bash
cd backend
mvn spring-boot:run
```

Бэкенд читает статьи из `../knowledge` (корень монорепы). Порт `8080`.

Терминал 2:

```bash
cd frontend
npm install
npm run dev
```

Фронт: http://localhost:5173. Vite проксирует `/api` на `http://localhost:8080`.

## Тесты

### Unit / slice бэкенда

```bash
mvn -pl backend test
```

### API-тесты (RestAssured, поднимают Spring Boot на random port)

```bash
mvn -pl tests -am test -Dtest='!*UiTest'
```

### UI-тесты (Selenide, нужны запущенные фронт и бэк)

Сначала поднимите стенд (`npm run dev` + `mvn spring-boot:run` или Docker), затем:

```bash
# локальный Vite
mvn -pl tests test -Dtest='*UiTest' -Dui.baseUrl=http://localhost:5173

# Docker (фронт на :80)
mvn -pl tests test -Dtest='*UiTest' -Dui.baseUrl=http://localhost
```

Headless: `-Dselenide.headless=true`.

Allure: результаты в `tests/target/allure-results`.

CI гоняет backend unit + API-тесты, UI пропускает (нужен браузер и живой фронт).

## ELK (не нужен для обычного запуска)

`docker compose up --build` **не** поднимает Elasticsearch/Logstash/Kibana.

```bash
SPRING_PROFILES_ACTIVE=prod,elk docker compose --profile elk up --build
```

Kibana: http://localhost:5601. Без профиля `elk` логи только в консоль бэкенда.

## Ручные тест-кейсы

- `docs/test-cases/ProductsAPI_TestCases.md`
- `docs/test-cases/json-formatter-test-documentation.md`
