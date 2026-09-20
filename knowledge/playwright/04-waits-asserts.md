# Ожидания и проверки в Playwright

📝 Playwright решает главную боль UI-тестов- flaky (нестабильные) тесты. Для этого он ждёт элементы автоматически, а проверки делает через модуль expect с web-first assertions.

## Автоожидания

📝 Playwright сам ждёт, что элемент готов к действию. Ничего писать не нужно.

    page.get_by_role("button", name="Войти").click()

Перед click Playwright сам проверит:

- Элемент привязан к DOM
- Элемент видим
- Элемент стабилен (не анимируется)
- Элемент не перекрыт другим элементом
- Элемент активен (enabled)

💡 В Selenium ты бы писал явные ожидания через WebDriverWait на каждое действие. В Playwright- просто кликаешь.

## Ручные ожидания

📝 Иногда автоожиданий мало. Тогда используем явные ожидания.

- locator.wait_for(state="visible") — ждать, пока элемент появится
- locator.wait_for(state="hidden") — ждать, пока исчезнет
- page.wait_for_load_state("networkidle") — ждать, пока стихнут сетевые запросы
- page.wait_for_response(url) — ждать конкретный ответ API
- page.wait_for_url(url) — ждать переход на URL
- page.wait_for_timeout(ms) — жёсткий сон (антипаттерн!)

    page.get_by_role("button", name="Сохранить").click()
    page.wait_for_response("**/api/notes")  # ждём, что ушёл запрос
    expect(page.get_by_text("Сохранено")).to_be_visible()

⚠️ wait_for_timeout — зло: тест работает медленнее и всё равно может падать. Всегда ждём событие, а не время.

## Модуль expect

📝 expect — это не питоновский assert, а специальные web-first assertions. Они опрашивают условие несколько раз до таймаута.

    from playwright.sync_api import expect

    expect(page.get_by_text("Буся")).to_be_visible()

- Если элемент не появился сразу — expect будет проверять снова и снова до 5 секунд (по умолчанию)
- Если условие выполнилось за 100 мс — тест идёт дальше
- При провале- показывает детальную причину

💡 Это и есть секрет стабильности: тест не падает, если сервер чуть задумался.

## Проверки видимости

- to_be_visible() — элемент есть и видим
- to_be_hidden() — элемент в DOM, но скрыт (display: none, opacity: 0, за пределами экрана)
- to_be_attached() — элемент есть в DOM (может быть невидимым)

⚠️ to_be_hidden vs not_to_be_visible — разные вещи:
- to_be_hidden — элемент существует, но скрыт (например, модалка закрыта)
- not_to_be_visible — элемент может вообще не существовать в DOM

    # модалка была и закрылась
    expect(modal).to_be_hidden()

    # элемента вообще нет на странице
    expect(missing).not_to_be_visible()

## Проверки текста

- to_have_text("Буся") — точное совпадение текста
- to_contain_text("кот") — содержит подстроку
- to_have_text(re.compile(r"\d+")) — регулярка

    expect(page.get_by_role("heading")).to_have_text("Список статей")
    expect(page.get_by_role("article").first).to_contain_text("REST")

⚠️ to_have_text проверяет только видимый текст, без HTML-разметки.

## Проверки полей форм

- to_have_value("значение") — текущее value у input
- to_be_empty() / not_to_be_empty()
- to_be_checked() — для checkbox/radio
- to_be_focused() — в фокусе
- to_be_enabled() / to_be_disabled()

    page.get_by_label("Телефон").fill("+79991234567")
    expect(page.get_by_label("Телефон")).to_have_value("+79991234567")

    page.get_by_role("checkbox", name="Запомнить").check()
    expect(page.get_by_role("checkbox", name="Запомнить")).to_be_checked()

## Проверки URL и заголовка

- to_have_url("http://localhost/notes")
- to_have_title("QA Universe")
- to_have_url(re.compile("/notes/\\d+"))

    page.get_by_role("button", name="Войти").click()
    expect(page).to_have_url("http://localhost/notes")

## Проверки количества

- to_have_count(n) — ровно n элементов найдено

    expect(page.get_by_role("listitem")).to_have_count(5)
    expect(page.get_by_text("Error")).to_have_count(0)  # нет ошибок

## Проверки атрибутов и классов

- to_have_attribute("href", "/login")
- to_have_class("active")
- to_have_id("main-form")
- to_have_css("color", "rgb(255, 0, 0)")

## Таймауты

📝 По умолчанию каждая expect-проверка ждёт 5 секунд. Можно переопределить.

    expect(locator).to_be_visible(timeout=10000)  # 10 секунд

Глобально для всех тестов в pytest.ini:

    [pytest]
    playwright_default_timeout = 10000

⚠️ Если тест падает из-за таймаута — это почти всегда проблема приложения, а не теста. Увеличение таймаута скрывает баг.

## Негативные проверки

📝 Почти каждое assertion имеет обратную форму через not_.

    expect(locator).not_to_be_visible()
    expect(locator).not_to_have_text("Error")
    expect(locator).not_to_be_checked()

💡 В тестах проверяем, что ошибки нет: expect(page.get_by_text("error")).to_have_count(0).

## Практический пример: тест логина

    def test_login():
        page.goto("http://localhost/login")

        page.get_by_label("Телефон").fill("+79991234567")
        page.get_by_label("Пароль").fill("password123")
        page.get_by_role("button", name="Войти").click()

        # ждём URL главной
        expect(page).to_have_url("http://localhost/notes")

        # заголовок страницы
        expect(page).to_have_title("QA Universe")

        # виден список статей
        expect(page.get_by_role("heading", name="Заметки")).to_be_visible()

        # есть хотя бы одна статья
        expect(page.get_by_role("article")).not_to_have_count(0)

        # ошибки нет
        expect(page.get_by_text("Error")).to_have_count(0)

💡 Ни одного wait_for_timeout, ни одного sleep- только семантические ожидания. Тест стабилен.

🎯 Вопросы с собеса:

- Что такое автоожидания в Playwright? (библиотека сама ждёт готовности элемента перед действием: видимость, стабильность, активность)
- Чем expect отличается от обычного assert? (expect опрашивает условие много раз до таймаута- не падает из-за медленного сервера)
- Почему wait_for_timeout — антипаттерн? (замедляет тест и не гарантирует, что нужное состояние наступило)
- Чем to_be_hidden отличается от not_to_be_visible? (to_be_hidden- элемент есть, но скрыт; not_to_be_visible- может не быть в DOM)
- Зачем to_have_count(0)? (проверить отсутствие элемента- для негативных сценариев)
- Как настроить таймаут глобально? (pytest.ini: playwright_default_timeout)