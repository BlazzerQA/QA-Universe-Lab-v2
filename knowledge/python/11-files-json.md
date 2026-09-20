# Файлы и JSON в Python

📝 Работа с файлом: открыть → операции → закрыть. Оператор with закрывает файл сам, даже если случилась ошибка.

## Открытие файла

    f = open("report.txt", "r", encoding="utf-8")
    data = f.read()
    f.close()

Режимы:

- "r" — чтение (по умолчанию); файла нет → FileNotFoundError
- "w" — запись; создаёт файл или СТИРАЕТ содержимое (!)
- "a" — дописывание в конец
- "x" — создание; если файл уже есть → ошибка
- "b" — бинарный режим: "rb", "wb"

⚠️ "w" стирает файл сразу при открытии. Потерял отчёт- проверь режим.

## Контекстный менеджер with

📝 with гарантирует закрытие файла, даже если внутри блока упало исключение.

    with open("report.txt", "r", encoding="utf-8") as f:
        data = f.read()
    # здесь файл уже закрыт

💡 В тестах всегда используй with: незакрытый файл блокируется на Windows и течёт дескрипторами.

## Чтение

- f.read() — весь файл одной строкой
- f.readline() — одна строка
- f.readlines() — список строк
- for line in f — построчное чтение (экономит память)

💡 Разбор логов: for line in f: if "ERROR" in line: ...

## Запись

    with open("out.txt", "w", encoding="utf-8") as f:
        f.write("строка 1\n")
        f.writelines(["строка 2\n", "строка 3\n"])

⚠️ write не добавляет перенос строки сам- \n на твоей совести.

## JSON

📝 json- модуль для преобразования между объектами Python и JSON.

    import json

    # объект Python → строка JSON
    s = json.dumps({"status": 200, "ok": True})

    # строка JSON → объект Python
    d = json.loads(s)

    # запись в файл
    with open("data.json", "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

    # чтение из файла
    with open("data.json", encoding="utf-8") as f:
        data = json.load(f)

- dump / load — работают с файлом
- dumps / loads — работают со строкой
- ensure_ascii=False — кириллица остаётся читаемой, а не \u043a\u043e...
- indent=2 — человекочитаемое форматирование

💡 В тестах: data = response.json() — это тот же loads под капотом. Ожидаемые данные хранят в .json файлах и читают через json.load.

⚠️ Соответствие типов: true → True, null → None, object → dict, array → list.

## pathlib (современный способ)

    from pathlib import Path

    p = Path("reports") / "run.json"
    text = p.read_text(encoding="utf-8")
    p.write_text(text, encoding="utf-8")
    p.exists()  # True/False

💡 Path сам склеивает пути с нужными слэшами под Windows и Linux- не думай о разделителях.

🎯 Вопросы с собеса:

- Чем "r" отличается от "w"? (чтение против записи со стиранием)
- Зачем нужен with? (гарантирует закрытие файла даже при исключении)
- Чем json.load отличается от json.loads? (файл против строки)
- Что делает ensure_ascii=False? (кириллица остаётся читаемой)
- Как прочитать огромный файл без загрузки в память? (построчно: for line in f)