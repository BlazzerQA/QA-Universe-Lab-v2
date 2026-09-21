# Локаторы в Playwright

📝 Локатор- способ найти элемент на странице, чтобы с ним взаимодействовать. Playwright предлагает современные локаторы, ориентированные на пользователя, а не на вёрстку.

## Модель: page.locator()

📝 Любое действие с элементом начинается с локатора.

    page.locator("button").click()

Но это базовый вариант. Playwright предлагает **рекомендуемые локаторы**, привязанные к роли и содержимому, а не к CSS-селекторам.

## Рекомендуемые локаторы

- **get_by_role(role)** — по ARIA-роли: button, link, heading, checkbox, textbox, dialog и т.д.
- **get_by_text(text)** — по видимому тексту
- **get_by_label(text)** — по label поля формы (самое важное для input!)
- **get_by_placeholder(text)** — по placeholder атрибуту
- **get_by_alt_text(text)** — по alt у картинок
- **get_by_title(text)** — по title атрибуту
- **get_by_test_id(id)** — по data-testid (для элементов, которые не видно пользователю)

Примеры:

    page.get_by_role("button", name="Войти").click()
    page.get_by_label("Телефон").fill("+79991234567")
    page.get_by_placeholder("Введите пароль").fill("password123")
    page.get_by_text("Буся и Машка").click()
    page.get_by_role("heading", name="API").is_visible()

💡 Эти локаторы ломаются реже: пока пользователь видит кнопку "Войти", локатор работает. Сменили цвет/класс/внутреннюю структуру- локатор жив.

## get_by_role с деталями

📝 get_by_role — самый мощный. Кроме name принимает:

    page.get_by_role("button", name="Отправить")
    page.get_by_role("checkbox", name="Запомнить меня").check()
    page.get_by_role("link", name="Home").click()
    page.get_by_role("heading", level=2)  # h2

⚠️ Роли берутся из ARIA или из семантики HTML. button, a, input имеют роли по умолчанию; div- нет.

## get_by_test_id

📝 Для технических элементов, которые пользователь не видит: скрытые маркеры, элементы аналитики.

    # HTML: <div data-testid="note-card-1">...</div>
    page.get_by_test_id("note-card-1").click()

💡 Конвенция в команде: пользовательские элементы- по role/text/label; только для тестов добавляем data-testid.

## CSS и XPath

📝 Используются, когда рекомендуемых локаторов не хватает.

    page.locator("button.submit").click()         # CSS
    page.locator(".header > .logo img").click()   # CSS с иерархией
    page.locator("//button[@id='submit']").click()  # XPath
    page.locator("#login-form input[name='phone']").fill(...)

⚠️ Длинные CSS-цепочки и XPath- зло: меняешь класс вёрстки- ломается десяток тестов. Playwright советует их только как последний вариант.

## Фильтрация и цепочки

📝 Когда элементов много- фильтруем.

    page.get_by_role("listitem").filter(has_text="Буся").click()
    page.get_by_role("article").filter(has=page.get_by_role("heading", name="API"))

- has_text — содержит текст
- has — содержит элемент-локатор

Цепочки локаторов (поиск внутри найденного):

    row = page.get_by_role("row", name="Буся")
    row.get_by_role("button", name="Удалить").click()

💡 Это то же самое, что человек делает мышью: "нашёл строку, в ней — кнопку".

## first / last / nth

    page.get_by_role("listitem").first.click()
    page.get_by_role("listitem").last.click()
    page.get_by_role("listitem").nth(2).click()  # третий, индекс с 0

⚠️ nth хрупкий: порядок элементов может поменяться. Используй только когда других вариантов нет (например, списки-гридлы без уникальных текстов).

## Антипаттерны

❌ Длинные CSS: `page.locator("div.main > section > ul > li:nth-child(2) > span")` — ломается при любом рефакторинге.

❌ XPath с осями: `//div[@class='root']/div[2]/span` — то же самое.

❌ Завязка на внутренние классы: `page.locator(".