# INNER JOIN

`INNER JOIN` (или просто `JOIN`) возвращает **только строки, у которых нашлась пара**. Нет совпадения по `ON` — строки нет в результате.

Это значение по умолчанию: написали `JOIN` без слова — это INNER.

## Синтаксис

```sql
SELECT c.full_name, o.order_id, o.amount
FROM customers c
INNER JOIN orders o ON c.customer_id = o.customer_id;
```

То же самое:

```sql
SELECT c.full_name, o.order_id, o.amount
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id;
```

## Как думать

Клиент без заказов **не попадёт** в выборку. Заказ без клиента — тоже.

| customers | orders | INNER |
|---|---|---|
| Анна, есть заказ | заказ Анны | да |
| Вера, заказов нет | — | нет |
| — | заказ без клиента | нет |

## Когда использовать

Нужны только «живые» связи: оплаченные заказы с именем покупателя, товары, у которых есть категория.

Для проверки данных в тестах INNER как раз отсекает дыры. Если хочешь увидеть дыры — бери `LEFT JOIN`.

## Фильтр после соединения

```sql
SELECT c.full_name, o.amount
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
WHERE o.status = 'paid'
ORDER BY o.amount DESC;
```

Здесь `WHERE` безопасен: INNER и так не держит пустые правые строки.

## Старый синтаксис (знать, не писать)

```sql
SELECT c.full_name, o.amount
FROM customers c, orders o
WHERE c.customer_id = o.customer_id;
```

Это тот же INNER, но условие легко забыть — получится `CROSS JOIN`. Современный код пишет `JOIN ... ON`.
