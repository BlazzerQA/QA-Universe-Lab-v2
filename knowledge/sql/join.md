# SQL: JOIN

`JOIN` соединяет строки из двух (и больше) таблиц по условию. Вместо двух разрозненных списков получаешь одну выборку: клиент и его заказ, товар и категория.

Без JOIN пришлось бы руками сопоставлять `id`. База делает это сама.

## Синтаксис

```sql
SELECT columns
FROM table_a
JOIN table_b ON table_a.id = table_b.a_id;
```

`ON` — условие связи. Чаще всего это внешний ключ: `orders.customer_id = customers.customer_id`.

## Зачем нужны алиасы

Имена колонок повторяются (`id`, `city`). Алиас сокращает запись и снимает неоднозначность:

```sql
SELECT c.full_name, o.amount
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id;
```

Если не написать `c.full_name`, а просто `full_name` — ок, пока колонка уникальна. Как только одно имя есть в обеих таблицах — будет ошибка, нужна таблица или алиас.

## Виды JOIN

| Вид | Что остаётся в результате |
|---|---|
| `INNER JOIN` | Только совпавшие строки |
| `LEFT JOIN` | Все строки левой таблицы + совпадения справа (иначе `NULL`) |
| `RIGHT JOIN` | Все строки правой таблицы + совпадения слева (иначе `NULL`) |
| `FULL JOIN` | Все строки обеих таблиц, без пары — `NULL` |
| `CROSS JOIN` | Декартово произведение, без `ON` |
| `SELF JOIN` | Таблица соединяется сама с собой |

`LEFT JOIN` = `LEFT OUTER JOIN`, то же для `RIGHT` и `FULL`. Слово `OUTER` можно не писать.

## ON vs WHERE

Для `INNER JOIN` фильтр в `ON` и в `WHERE` часто даёт один результат.

Для `LEFT`/`RIGHT`/`FULL` — нет:

- условие в `ON` решает, **с чем склеить** строку;
- условие в `WHERE` фильтрует **уже склеенный** результат и может выкинуть строки с `NULL`, превратив LEFT в INNER.

```sql
-- клиенты без заказов пропадут: WHERE убьёт NULL
SELECT c.full_name, o.order_id
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.status = 'paid';
```

```sql
-- клиенты без paid-заказов останутся, у них order_id будет NULL
SELECT c.full_name, o.order_id
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id AND o.status = 'paid';
```

## USING и NATURAL

```sql
-- если колонка связи называется одинаково в обеих таблицах
SELECT *
FROM customers
JOIN orders USING (customer_id);
```

`NATURAL JOIN` склеивает все одноимённые колонки сам. На собесе достаточно знать, что он неявный и опасный: добавили колонку `city` в обе таблицы — связь внезапно изменилась. В работе почти не используют, пишут явный `ON`.

## Несколько JOIN подряд

```sql
SELECT c.full_name, o.order_id, p.product_name
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
JOIN order_items i ON o.order_id = i.order_id
JOIN products p ON i.product_id = p.product_id;
```

Читается слева направо: сначала клиент+заказ, потом к этой строке клеится следующая таблица.

## Что спрашивают на собесе

- Чем `INNER` отличается от `LEFT`.
- Почему после `LEFT JOIN` в `WHERE` по правой таблице «пропали» пустые строки.
- Что такое декартово произведение и чем опасен `CROSS JOIN` без нужды.
- Как найти записи «слева без пары справа»: `LEFT JOIN ... WHERE right.id IS NULL`.
- SQLite (как в этой лабе): `RIGHT` и `FULL` есть с версии 3.39. Если их нет — `RIGHT` меняют местами таблицы и пишут `LEFT`, `FULL` собирают через `UNION`.
