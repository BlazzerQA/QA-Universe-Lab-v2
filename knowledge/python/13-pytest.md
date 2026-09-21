# pytest: первый тестовый фреймворк

📝 pytest- фреймворк для написания и запуска тестов на Python. Тест- обычная функция, проверка- обычный assert.

## Первый тест

    def test_login_success():
        assert login("79991234567", "password123") == 200

- Файл теста: test_*.py или *_test.py
- Функция теста: имя начинается с test_
- Проверка: assert условие; если False- тест упал

Запуск:

    pytest            # все тесты в папке
    pytest -v         # подробно: каждый тест отдельной строкой
    pytest -k "login" # только тесты с login в имени
    pytest -x         # стоп на первом падении
    pytest tests/test_api.py::test_login_success  # один тест

💡 Код выхода: 0- всё зелёное, 1- есть падения. На этом строятся проверки в CI.

## assert и сообщения

    assert status == 200, f"ожидал 200, получил {status}"

📝 Второй аргумент assert- сообщение при падении. Без него pytest сам покажет диф, но с сообщением лог понятнее.

Частые формы:

- assert a == b
- assert x in my_list
- assert not errors (список пуст)
- with pytest.raises(ValueError): ... — проверка, что исключение брошено

## Фикстуры

📝 Фикстура- подготовка и очистка для теста: создать данные до, убрать после.

    import pytest

    @pytest.fixture
    def user():
        u = create_user()   # подготовка
        yield u             # отдать тесту
        delete_user(u)      # очистка после теста

    def test_get_user(user):
        assert get_user(user["id"])["name"] == user["name"]

- Фикстура запрашивается по имени аргумента
- scope: function (по умолчанию), class, module, session- как часто пересоздавать
- conftest.py- файл с фикстурами, видимыми всей папке

💡 Типичные фикстуры в API-тестах: base_url (session), auth_token (session), test_user (function).

⚠️ Фикстура с yield чистит даже если тест упал. С return очистки не будет вообще.

## Parametrize

📝 parametrize гоняет один тест с разными данными.

    @pytest.mark.parametrize("phone,password,expected", [
        ("79991234567", "password123", 200),
        ("79991234567", "wrong", 401),
        ("", "password123", 400),
    ])
    def test_login(phone, password, expected):
        assert login(phone, password) == expected

💡 Это негативные сценарии из твоей QA-базы, ставшие кодом: один тест, три кейса, три строки в отчёте.

## Marks

- @pytest.mark.skip(reason="ещё не готово") — пропустить
- @pytest.mark.skipif(condition, reason="...") — пропустить при условии
- @pytest.mark.xfail — “известный баг, ожидаем падение”
- Свои метки: @pytest.mark.smoke и запуск pytest -m smoke

    pytest -m smoke       # только smoke
    pytest -m "not slow"  # всё кроме медленных

💡 Метками делят сюиты: smoke на пре-мерж, regression ночью.

## conftest.py

📝 conftest.py- файл с общими фикстурами и хуками папки. pytest подхватывает его сам, импорты не нужны.

💡 Структура проекта: корневой conftest с base_url и сессионными фикстурами; в папках разделов- свои conftest.

🎯 Вопросы с собеса:

- Как pytest находит тесты? (файлы test_*.py, функции test_*)
- Что такое фикстура и зачем yield вместо return? (подготовка/очистка; yield даёт очистку после теста)
- Что значит scope фикстуры? (частота пересоздания: function/class/module/session)
- Зачем parametrize? (один тест с разными наборами данных)
- Чем skip отличается от xfail? (сознательно не гоняем против известный баг, ждём падение)
- Зачем conftest.py? (общие фикстуры папки, подхватываются автоматически)