# pytest-playwright: связка с pytest

📝 pytest-playwright- официальный плагин, который подключает Playwright к pytest: даёт готовые фикстуры, сам запускает браузер и собирает артефакты.

Установка:

    pip install pytest pytest-playwright

## Встроенные фикстуры

📝 Плагин даёт готовые фикстуры, которые можно подставить прямо в тест.

- browser — браузер (chromium/firefox/webkit)
- context — изолированный профиль (свои куки и хранилище)
- page — вкладка внутри контекста

    def test_home(page):
        page.goto("http://localhost/notes")
        expect(page).to_have_title("QA Universe")

Всё- ни тебе sync_playwright(), ни launch: плагин делает это сам.

⚠️ По умолчанию на каждый тест создаётся новый контекст и страница: логины и куки не перетекают между тестами. Это та самая изоляция из статьи 2.

## Запуск тестов

    pytest                     # все тесты, без окна браузера
    pytest --headed            # с окном браузера
    pytest --browser firefox   # в Firefox
    pytest --browser chromium --browser firefox  # в двух браузерах
    pytest --slowmo 500        # замедлить действия на 500 мс (отладка глазами)

💡 С --headed удобно отлаживаться: видно, как тест идёт по шагам, и понятно, где именно он падает.

## Изменение фикстур в conftest.py

📝 Фикстуры плагина можно переопределить в conftest.py — на весь проект.

Размер окна и локаль для всех тестов:

    import pytest

    @pytest.fixture(scope="session")
    def browser_context_args(browser_context_args):
        return {
            **browser_context_args,
            "viewport": {"width": 1280, "height": 720},
            "locale": "ru-RU",
            "timezone_id": "Europe/Moscow",
        }

Фикстура залогиненной страницы:

    @pytest.fixture
    def authed_page(page):
        page.goto("http://localhost/login")
        page.get_by_label("Телефон").fill("+79991234567")
        page.get_by_label("Пароль").fill("password123")
        page.get_by_role("button", name="Войти").click()
        return page

💡 Классика: фикстура делает логин, а тесты, которым нужен авторизованный пользователь, просто просят её по имени.

## base_url

📝 Чтобы не писать полный адрес в каждом тесте:

    # conftest.py
    @pytest.fixture(scope="session")
    def base_url():
        return "http://localhost"

    # в тестах
    def test_home(page):
        page.goto("/notes")  # прибавится к base_url

💡 Тогда переключение окружения (локалка / дев / стейдж) делается в одном месте, а не в сотне тестов.

## Артефакты при падении

📝 Плагин умеет сохранять скриншоты, видео и trace- запись происходящего в браузере.

    pytest --screenshot only-on-failure  # скрин при падении
    pytest --video retain-on-failure     # видео упавших тестов
    pytest --tracing retain-on-failure   # trace упавших тестов

Артефакты:

- скриншот — картинка страницы в момент падения
- видео — запись теста в .webm
- trace — самый мощный: снимки DOM, сеть, логи и действия по шагам

## Trace: разбор падений постфактум

📝 Trace- полная запись теста: снимки DOM на каждом шаге, сетевые запросы, консольные логи.

    pytest --tracing retain-on-failure

При падении появится файл вида trace.zip. Смотрим:

    playwright show-trace trace.zip

💡 Открывается вьюер в браузере: можно “перемотать” падение, посмотреть состояние страницы на любом шаге, запросы и ошибки в консоли. Разбор флакающего теста занимает минуты, а не часы.

⚠️ Трейсы весят много. На CI их обычно хранят как артефакты 7-14 дней, потом чистят.

## Пример структуры проекта

    autotests/
    ├── conftest.py        # base_url, browser_context_args, authed_page
    ├── test_login.py      # тесты логина
    ├── test_notes.py      # тесты статей
    └── pytest.ini         # флаги и метки

Пример pytest.ini:

    [pytest]
    addopts = --screenshot only-on-failure --video retain-on-failure --tracing retain-on-failure

💡 С таким конфигуром любое падение на CI сразу даёт три артефакта для разбора.

🎯 Вопросы с собеса:

- Какие фикстуры даёт плагин? (browser, context, page)
- Зачем новый контекст на каждый тест? (изоляция: куки, хранилище и логины не перетекают между тестами)
- Как запустить тесты в Firefox? (--browser firefox)
- Как переопределить параметры контекста? (фикстура browser_context_args в conftest.py)
- Что такое trace и как его смотреть? (запись теста: DOM, сеть, логи; команда playwright show-trace trace.zip)
- Как включить артефакты только при падении? (--screenshot only-on-failure, --video retain-on-failure, --tracing retain-on-failure)