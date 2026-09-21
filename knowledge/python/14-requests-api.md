# requests: бьём по API из Python

📝 requests- библиотека для HTTP-запросов из Python. То же самое, что Postman, но кодом- основа API-автотестов.

Установка:

    pip install requests

## GET запрос

    import requests

    r = requests.get("http://localhost:8080/api/notes")
    print(r.status_code)  # 200
    print(r.json())       # тело ответа как dict/list

- r.status_code — статус-код (те самые 200/404/500 из твоей QA-базы)
- r.json() — распарсить тело как JSON (dict/list)
- r.text — тело строкой
- r.headers — заголовки ответа

💡 Это вкладка Network из DevTools, но кодом: метод, URL, статус, тело- всё в одном объекте.

## Параметры и заголовки

    r = requests.get(
        "http://localhost:8080/api/notes",
        params={"tag": "api"},           # ?tag=api в URL
        headers={"Accept": "application/json"},
    )

📝 params- query-параметры (вкладка Params в Postman), headers- заголовки запроса.

## POST с телом

    r = requests.post(
        "http://localhost:8080/api/auth/login",
        json={"phone": "+79991234567", "password": "password123"},
    )
    token = r.json()["token"]

📝 json=... сериализует dict в JSON и сам ставит Content-Type: application/json. data=... — для форм и сырых строк.

💡 Полученный токен кладут в фикстуру session и передают в следующие запросы через headers.

## PUT / PATCH / DELETE

    requests.put(url, json={...})
    requests.patch(url, json={...})
    requests.delete(url)

📝 Те же методы из API-базы: PUT заменяет целиком, PATCH- частично, DELETE удаляет.

## Session

    s = requests.Session()
    s.headers.update({"Authorization": f"Bearer {token}"})
    r1 = s.get(BASE + "/api/notes")        # заголовок применится сам
    r2 = s.post(BASE + "/api/notes", json={...})

📝 Session хранит заголовки и куки между запросами- как браузер, в котором ты залогинен.

💡 В тестах Session создают в фикстуре: логин один раз, используют все тесты.

## Таймауты и ошибки

    r = requests.get(url, timeout=5)  # секунды

⚠️ Без timeout запрос может висеть вечно и завесить весь прогон. timeout ставим всегда.

- requests.ConnectionError — сервер недоступен
- requests.Timeout — не дождались ответа за timeout
- r.raise_for_status() — бросить исключение, если статус 4xx/5xx

    try:
        r = requests.get(url, timeout=5)
        r.raise_for_status()
    except requests.Timeout:
        print("сервер не ответил вовремя")

💡 raise_for_status- удобный guard: вместо if status != 200 в каждом тесте.

## Пример: полноценный API-тест

    def test_login_and_get_notes():
        r = requests.post(
            BASE + "/api/auth/login",
            json={"phone": PHONE, "password": PASSWORD},
            timeout=5,
        )
        assert r.status_code == 200, f"login failed: {r.text}"
        token = r.json()["token"]

        r = requests.get(
            BASE + "/api/notes",
            headers={"Authorization": f"Bearer {token}"},
            timeout=5,
        )
        assert r.status_code == 200
        assert isinstance(r.json(), list)

💡 Это уже настоящий автотест проекта друга. Оберни в pytest- и можно класть в папку autotests/ репозитория.

🎯 Вопросы с собеса:

- Как получить тело ответа в JSON? (r.json())
- Чем json= отличается от data=? (dict → JSON с авто-Content-Type против сырых данных/формы)
- Зачем timeout? (чтобы зависший сервер не повесил весь прогон)
- Что делает raise_for_status()? (бросает исключение при 4xx/5xx)
- Зачем Session? (заголовки и куки между запросами, как залогиненный браузер)
- Как проверить статус 201? (r.status_code == 201)