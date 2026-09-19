# HTML vs XML vs JSON

📝 HTML- создание веб-страниц, XML и JSON- хранение и передача данных.

## HTML (Hypertext Markup Language)

- Язык разметки для гипертекста
- Используется для создания веб-страниц и отображении информации в браузере
- Есть набор тегов для определения заголовков, абзацев и тд

Пример HTML:

    <!DOCTYPE html>
    <html>
    <head>
    <title>My Web Page</title>
    </head>
    <body>
    <h1>Welcome to my website!</h1>
    </body>
    </html>

## XML (Extensible Markup Language)

- Язык разметки для структурирования данных
- Используется для обмена данными между клиентом и сервером
- Возможность создавать собственные теги для описания данных
- Представляет данные в виде дерева тегов

Пример XML:

    <?xml version="1.0" encoding="UTF-8"?>
    <book>
    <title>The Great Gatsby</title>
    <author>F. Scott Fitzgerald</author>
    <publication_year>1925</publication_year>
    <genre>Fiction</genre>
    <description>A novel about USA</description>
    </book>

## JSON (JavaScript Object Notation)

- Легкий формат обмена данными на основе синтаксиса JavaScript
- Используется для обмена данными между клиентом и сервером
- Представляет структурированные данные в виде ключ-значение или массивов
- Легко читаемый для человека. Компактнее, чем XML, требует меньше места

Пример JSON:

    {
    "name": "John Doe",
    "age": 35,
    "email": "john.doe@example.com",
    "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "zip": "12345"
    },
    "hobbies": ["reading", "hiking", "photography"]
    }

💡 В проекте друга: страницы сайта — это HTML, а бэкенд отдаёт статьи в JSON (ответ /api/notes). XML не используется — и это норма: JSON выиграл войну форматов.