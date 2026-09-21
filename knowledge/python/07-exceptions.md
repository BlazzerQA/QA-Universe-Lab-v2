# Исключения в Python

📝 Исключение (exception)- ошибка во время выполнения программы. Python бросает исключение, когда встречает некорректную операцию. Если его не обработать- программа падает.

## try / except / else / finally

    try:
        result = 10 / 0
    except ZeroDivisionError:
        print("деление на ноль")
    else:
        print("ошибок не было")  # выполнится, если в try не было исключений
    finally:
        print("выполнится всегда")  # закрытие ресурсов

- try- блок с подозрительным кодом
- except- перехват конкретного исключения
- else- выполнится, если в try всё прошло без ошибок
- finally- выполнится в любом случае (даже при return или новом исключении)

💡 finally используют для освобождения ресурсов: закрытия файлов, соединений, браузера в Playwright.

⚠️ Голый except (без имени)- антипаттерн: перехватывает ВСЁ, включая Ctrl+C и системные сигналы. Ловите только то, что реально может упасть.

## Популярные исключения

- ValueError- неправильное значение (int("abc"))
- TypeError- неправильный тип (2 + "2")
- KeyError- несуществующий ключ словаря
- IndexError- индекс за пределами списка
- AttributeError- у объекта нет такого атрибута/метода
- FileNotFoundError- файл не найден
- AssertionError- не прошла проверка assert (база всех pytest-фейлов)
- TimeoutError- таймаут ожидания
- ConnectionError- проблема с сетью

💡 В тестах часто перехватывают TimeoutError (элемент не появился) и ConnectionError (сервер не отвечает), чтобы сделать ретрай.

## raise

📝 raise бросает исключение вручную.

    def divide(a, b):
        if b == 0:
            raise ValueError("b не может быть нулём")
        return a / b

💡 В тестах raise используют для проверки инвариантов: if status != 200: raise AssertionError(f"Ожидал 200, получил {status}").

## re-raise

📝 Внутри except можно выбросить то же исключение дальше (re-raise) — для логирования без проглатывания.

    try:
        ...
    except Exception as e:
        logger.error(f"Упало: {e}")
        raise  # пробрасываем дальше

## Свои исключения

📝 Создаются наследованием от Exception.

    class ApiError(Exception):
        def __init__(self, status, message):
            self.status = status
            super().__init__(f"API error {status}: {message}")

    raise ApiError(404, "user not found")

💡 В AQA-фреймворке полезно делать иерархию: BaseTestError → LoginError, TimeoutError, SelectorNotFound.

## Иерархия исключений

    BaseException
      ├── KeyboardInterrupt  (Ctrl+C)
      ├── SystemExit         (выход из программы)
      └── Exception          ← ловим только отсюда и вниз
            ├── ValueError
            ├── TypeError
            ├── AssertionError
            └── ...

⚠️ Никогда не ловите BaseException: вы потеряете возможность остановить программу через Ctrl+C.

## Лучшие практики

- Ловите только те исключения, которые реально можете осмысленно обработать
- В except указывайте максимально специфичный тип
- Логируйте исключение вместе с трейсбеком: logger.exception("ошибка")
- Не проглатывайте исключения молча (except: pass)- потом не найдёте баг
- Используйте контекстные менеджеры (with) для гарантированного освобождения ресурсов

🎯 Вопросы с собеса:

- Чем try/except отличается от if? (if проверяет условие ДО, except обрабатывает ошибку ПОСЛЕ её возникновения; иногда операцию дешевле попытаться сделать, чем заранее проверить- "easier to ask forgiveness than permission")
- Что делает finally? (выполняется всегда, даже при исключении и return)
- Зачем нужен else в try? (код, который должен выполниться только при успешном try, но до finally)
- Чем raise отличается от assert? (raise- произвольное исключение с любыми данными; assert- встроенный механизм для проверок, бросает AssertionError)
- Почему except Exception лучше, чем except? (не перехватывает KeyboardInterrupt и SystemExit)