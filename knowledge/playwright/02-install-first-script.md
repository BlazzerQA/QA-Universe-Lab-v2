# Установка и первый скрипт

📝 Установка Playwright идёт в два шага: сама библиотека, а потом браузеры, которыми она управляет.

## Установка

    pip install playwright
    playwright install

Первая команда ставит библиотеку, вторая скачивает в систему Chromium, Firefox и WebKit.

⚠️ Если нужен только Chrome (в CI часто хватает): playwright install chromium.

⚠️ В корпоративной сети playwright install может упереться в фаервол- нужен прокси.

## sync_api

📝 У Playwright два API: sync (последовательный, простой) и async (на asyncio). Для тестов начинаем с sync.

    from playwright.sync_api import sync_playwright

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        page = browser.new_page()
        page.goto("http://localhost")
        print(page.title())
        browser.close()

Разбор строк:

- sync_playwright() — подключение к библиотеке (with сам всё закроет)
- p.chromium.launch(headless=False) — запуск Chrome с видимым окном
- browser.new_page() — открыть новую вкладку
- page.goto(url) — переход по адресу
- page.title() — заголовок страницы
- browser.close() — закрыть браузер

💡 Сделай headless=True, и окно не появится: это режим для CI. Пока учишься, держи headless=False — видеть, как робот кликает, это половина опыта.

## Трёхэтажная модель: Browser → Context → Page

📝 browser- сам процесс браузера (самый тяжёлый). context — “профиль” со своими куками и хранилищем (как окно инкогнито). page — вкладка.

    browser = p.chromium.launch()
    context = browser.new_context()
    page = context.new_page()

- В одном браузере может быть много контекстов
- Контексты изолированы друг от друга: удобно для тестов с разными пользователями
- В одном контексте может быть несколько страниц

⚠️ В тестах не переиспользуй один контекст на все кейсы: логины и куки перетекут между тестами. Правильно- новый контекст на каждый тест.

## Куда запускать

Создай файл hello_playwright.py и выполни:

    python hello_playwright.py

Если проект друга поднят локально (docker compose up -d), укажи http://localhost — браузер откроет сайт с базой знаний.

## codegen: рекордер скриптов

📝 Playwright умеет записывать действия в код:

    playwright codegen http://localhost

Откроется браузер и панель генератора: ты кликаешь мышью, Playwright пишет код с локаторами и действиями. Отличный способ быстро набрать заготовки.

⚠️ Рекордер часто генерит уродливые локаторы (длинные цепочки CSS). Их чистят вручную на get_by_role/get_by_text — этому посвящена следующая статья.

## Типичные ошибки на старте

- Не выполнил playwright install → ошибка “browserType.launch: Executable doesn't exist”
- Сайт не запущен → page.goto падает с net::ERR_CONNECTION_REFUSED
- Линукс без графики или запуск в docker → только с headless=True

🎯 Вопросы с собеса:

- Из каких шагов состоит установка Playwright? (pip install playwright