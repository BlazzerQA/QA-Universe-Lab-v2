# QA Universe Lab — Frontend

Клиентская часть проекта QA Universe Lab на Vue 3 + Vite + Pinia + Vue Router.

## Стек

- Vue 3 (Composition API)
- Vite
- Pinia (state management)
- Vue Router
- Axios

## Запуск

```bash
npm install
npm run dev
```

Фронтенд будет доступен по адресу `http://localhost:5173`.

## Сборка

```bash
npm run build
```

## Структура

- `src/views/` — страницы приложения
- `src/components/` — переиспользуемые компоненты
- `src/stores/` — Pinia stores (auth)
- `src/api/` — HTTP клиент (Axios)
- `src/router/` — настройка маршрутизации

## Страницы

| Путь | Описание |
|------|----------|
| `/login` | Вход и регистрация |
| `/roadmap` | Карта обучения |
| `/json-formatter` | Форматирование и дерево JSON |
| `/products-ui` | CRUD таблица продуктов |
| `/profile` | Профиль пользователя |

## API прокси

В `vite.config.js` настроен прокси `/api` на `http://localhost:8080`.
