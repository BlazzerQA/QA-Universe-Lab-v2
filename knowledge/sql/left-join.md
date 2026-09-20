# LEFT JOIN

`LEFT JOIN` берёт **все строки левой** таблицы. Если справа пары нет — колонки правой таблицы будут `NULL`.

Левая таблица — та, что стоит в `FROM` (или слева от `JOIN`).

## Синтаксис

```sql
SELECT c.full_name, o.order_id, o.amount
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id;
```

Анна с заказами придёт несколько раз (по числу заказов). Вера без заказов придёт **один раз**, `order_id` и `amount` — `NULL`.

## Как думать

| customers (слева) | orders (справа) | LEFT |
|---|---|---|
| Анна | заказ Анны | Анна + заказ |
| Вера | нет заказа | Вера + NULL |
| — | заказ без клиента | нет (заказ не слева) |

## Типичная задача QA: найти «сирот»

Клиенты без заказов:

```sql
SELECT c.customer_id, c.full_name
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;
```

Проверка `IS NULL` именно по ключу правой таблицы (`order_id`), не по `customer_id` слева.

Так ловят баги: пользователь зарегистрировался, заказ не создался; товар без категории; тест оставил неполные данные.

## ON vs WHERE — главная ловушка

```sql
-- плохо, если цель — все клиенты: это уже не LEFT, а INNER
SELECT c.full_name, o.order_id
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.status = 'paid';
```

`WHERE o.status = 'paid'` выкидывает строки, где `o.status` это `NULL` (нет заказа). Вера исчезнет.

Чтобы оставить всех клиентов и подтянуть только paid-заказы:

```sql
SELECT c.full_name, o.order_id, o.amount
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id AND o.status = 'paid';
```

## Несколько совпадений

Один клиент, три заказа — в результате три строки с одним и тем же `full_name`. Это не баг JOIN, так устроена связь 1:N. Агрегат, если нужна одна строка на клиента:

```sql
SELECT c.full_name, COUNT(o.order_id) AS orders_count
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.full_name;
```

`COUNT(o.order_id)`, не `COUNT(*)`: иначе клиент без заказов получит 1 из-за строки с NULL.
