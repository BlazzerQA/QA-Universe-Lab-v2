# Декораторы и замыкания в Python

📝 Декоратор- функция, которая принимает другую функцию и возвращает её обёртку, расширяя поведение без изменения исходного кода. Замыкание- функция, которая помнит переменные из области видимости, где была создана.

## Замыкание (closure)

📝 Замыкание- функция, возвращаемая из другой функции, сохраняет доступ к её локальным переменным.

    def make_greeter(name):
        def greeter():
            print(f"Привет, {name}")
        return greeter

    greet_anna = make_greeter("Аня")
    greet_anna()  # "Привет, Аня"

💡 Пример из QA: фабрика проверок. def has_field(field): return lambda data: field in data — возвращает функцию, помнящую поле.

⚠️ Замыкание запоминает переменную по ссылке, а не по значению. Классическая ловушка в цикле: все функции будут ссылаться на последнее значение переменной.

## Декоратор

📝 Декоратор- функция, которая принимает функцию и возвращает новую функцию с добавленным поведением.

    def my_decorator(func):
        def wrapper():
            print("до вызова")
            func()
            print("после вызова")
        return wrapper

    @my_decorator
    def say_hi():
        print("hi")

📝 Синтаксис @my_decorator- сокращение: say_hi = my_decorator(say_hi).

💡 В pytest декораторами делают фикстуры: @pytest.fixture. Маркеры- это тоже декораторы: @pytest.mark.skip.

## Декоратор с аргументами функции

    def timer(func):
        def wrapper(*args, **kwargs):
            start = time.time()
            result = func(*args, **kwargs)
            print(f"{func.__name__} занял {time.time() - start:.2f}с")
            return result
        return wrapper

    @timer
    def slow_query():
        time.sleep(1)

⚠️ Без *args и **kwargs внутри wrapper декоратор не сможет работать с функциями, у которых есть аргументы.

## functools.wraps

📝 При декорировании у функции теряется имя (__name__) и docstring (__doc__) — они становятся теми, что у wrapper. functools.wraps копирует их обратно.

    import functools

    def my_decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            return func(*args, **kwargs)
        return wrapper

⚠️ Без wraps отладка превращается в ад: в трейсбеках вместо имени твоей функции будет “wrapper”.

## Декоратор с параметрами

📝 Декоратор с аргументами- это функция, возвращающая декоратор (тройная вложенность).

    def retry(times=3):
        def decorator(func):
            @functools.wraps(func)
            def wrapper(*args, **kwargs):
                for attempt in range(times):
                    try:
                        return func(*args, **kwargs)
                    except Exception:
                        if attempt == times - 1:
                            raise
            return wrapper
        return decorator

    @retry(times=5)
    def flaky_api_call():
        ...

💡 В AQA такой @retry вешают на тесты, которые падают из-за нестабильного API- он сам повторит попытку нужное число раз.

## Стек декораторов

📝 Если декораторов несколько, применяются они снизу вверх, а выполняются- сверху вниз.

    @first
    @second
    def func(): ...

📝 Эквивалентно: func = first(second(func)).

⚠️ Это частая путаница на собесе: порядок написания и порядок выполнения разные.

## Популярные примеры

- @pytest.fixture — фикстура в pytest
- @pytest.mark.skip — пропустить тест
- @pytest.mark.parametrize — параметризация
- @login_required — проверка авторизации в Django
- @staticmethod, @classmethod — типы методов класса

🎯 Вопросы с собеса:

- Что такое замыкание? (функция, помнящая переменные из области видимости, в которой создана)
- Что такое декоратор? (функция, принимающая функцию и возвращающая её обёртку)
- Как выглядит развёрнутая запись @decorator? (func = decorator(func))
- Зачем нужен functools.wraps? (сохраняет __name__ и __doc__ декорируемой функции)
- В каком порядке применяются декораторы при @first над @second? (сначала second, потом first — снизу вверх)
- Как сделать декоратор с аргументами? (вложенная функция возвращает декоратор, который возвращает wrapper)