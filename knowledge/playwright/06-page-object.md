# Page Object Model

📝 Page Object (POM)- архитектурный паттерн автотестов: каждая страница приложения представлена классом, в котором собраны локаторы и действия с ней. Тест читается как сценарий, а не как набор кликов.

## Зачем

- Читаемость: тест- это шаги пользователя, а не сырые локаторы
- Переиспользование: один логин используется в десятках тестов
- Поддержка: поменялась вёрстка- правим один класс страницы, а не сто тестов
- Разделение ответственности: тесты проверяют бизнес-логику, страницы знают, как это делать

💡 Без POM при редизайне проекта ты будешь чинить локаторы в каждом тесте. С POM — в одном месте.

## Три слоя

1. Тесты — что проверяем (бизнес-логика, проверки)
2. Страницы — как это делать (локаторы + методы)
3. Фикстуры — подготовка окружения (браузер, логин, данные)

## Базовый класс страницы

📝 Общая логика для всех страниц проекта выносится в базовый класс.

    class BasePage:
        def __init__(self, page):
            self.page = page

        def open(self, path=""):
            self.page.goto(path)

        def get_title(self):
            return self.page.title()

- self.page — объект вкладки, который пришёл из фикстуры
- Общие методы (открыть, заголовок) живут здесь

## Страница логина

    from playwright.sync_api import expect

    class LoginPage(BasePage):
        URL = "/login"

        def __init__(self, page):
            super().__init__(page)
            self.phone_input = page.get_by_label("Телефон")
            self.password_input = page.get_by_label("Пароль")
            self.submit_button = page.get_by_role("button", name="Войти")

        def fill_phone(self, phone):
            self.phone_input.fill(phone)

        def fill_password(self, password):
            self.password_input.fill(password)

        def click_submit(self):
            self.submit_button.click()

        def login(self, phone, password):
            self.open(self.URL)
            self.fill_phone(phone)
            self.fill_password(password)
            self.click_submit()

💡 Локаторы объявлены в __init__ один раз. Методы — действия пользователя. Композитный метод login() собирает всё в один шаг.

## Страница со статьями

    class NotesPage(BasePage):
        URL = "/notes"

        def __init__(self, page):
            super().__init__(page)
            self.heading = page.get_by_role("heading", name="Заметки")
            self.articles = page.get_by_role("article")
            self.error_message = page.get_by_text("Error")

        def open_notes(self):
            self.open(self.URL)

        def has_articles(self):
            return self.articles.count() > 0

        def get_article_count(self):
            return self.articles.count()

## Тест поверх страниц

    def test_login_shows_notes(authed_page):
        notes = NotesPage(authed_page)
        notes.open_notes()
        expect(notes.heading).to_be_visible()
        assert notes.get_article_count() > 0
        expect(notes.error_message).to_have_count(0)

    def test_login_invalid(phone, authed_page):
        login = LoginPage(authed_page)
        login.login("+79991234567", "wrong")
        expect(authed_page.get_by_text("неверный пароль")).to_be_visible()

💡 Тест стал читаться как чек-лист из твоей ручной базы: “логин → открыть заметки → проверить статьи → проверить отсутствие ошибок”.

## Фикстуры для страниц

📝 Чтобы не создавать объекты страниц руками в каждом тесте:

    # conftest.py
    import pytest

    @pytest.fixture
    def login_page(page):
        return LoginPage(page)

    @pytest.fixture
    def notes_page(authed_page):
        return NotesPage(authed_page)

Теперь в тестах:

    def test_notes(login_page, notes_page):
        login_page.login("+79991234567", "password123")
        expect(notes_page.heading).to_be_visible()

## Нюансы и антипаттерны

⚠️ Ассерты — вопрос религии:
- Классика: страницы только делают действия, ассерты живут в тестах
- Прагматика: страницы могут иметь методы-проверки (has_articles), но без pytest.assert

⚠️ Толстые страницы: если класс разросся до 500 строк — разбей на компоненты (модалки, меню — отдельные классы).

⚠️ Цепочки страниц: login_page.login().click_menu().open_profile() — соблазн, но ломает читаемость. Лучше два вызова в тесте.

⚠️ Не делай локаторы приватными: тестам иногда нужно напрямую проверить элемент (например, текст ошибки).

💡 Правило здравого смысла: сначала пиши тесты без POM. Когда видишь дублирование локаторов в трёх и более местах — выноси в страницу.

## Структура проекта

    autotests/
    ├── pages/
    │   ├── base_page.py
    │   ├── login_page.py
    │   └── notes_page.py
    ├── conftest.py
    ├── test_login.py
    └── test_notes.py

💡 Такое дерево — стандарт в индустрии. На собесе тебя попросят нарисовать структуру автотест-проекта — рисуй это.

🎯 Вопросы с собеса:

- Что такое Page Object? (класс, инкапсулирующий локаторы и действия одной страницы)
- Какие три слоя в архитектуре автотестов? (тесты, страницы, фикстуры)
- Где должны жить ассерты? (в тестах- классика; страницы только действия)
- Зачем базовый класс страницы? (общая логика: открытие, заголовок, ожидания)
- Когда создавать Page Object? (когда локаторы дублируются в 3+ тестах)
- Чем компоненты отличаются от страниц? (переиспользуемые блоки: модалка, меню- входят в разные страницы)