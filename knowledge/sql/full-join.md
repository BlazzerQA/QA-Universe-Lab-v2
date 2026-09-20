# FULL JOIN

`FULL JOIN` (`FULL OUTER JOIN`) — объединение LEFT и RIGHT: **все строки обеих** таблиц. Есть пара — склеиваются. Нет пары — с другой стороны `NULL`.

## Синтаксис

```sql
SELECT c.full_name, o.order_id, o.amount
FROM customers c
FULL JOIN orders o ON c.customer_id = o.customer_id;
```

В результате будут:

- клиенты с заказами;
- клиенты без заказов (`order_id` = NULL);
- заказы без клиента (`full_name` = NULL).

## Как думать

| customers | orders | FULL |
|---|---|---|
| Анна | заказ Анны | Анна + заказ |
| Вера | нет | Вера + NULL |
| нет | заказ-сирота | NULL + заказ |

INNER ⊂ LEFT ⊂ FULL, INNER ⊂ RIGHT ⊂ FULL.

## Когда использовать

Сверка двух списков: «что есть слева, справа и в обоих». В тестах — полный дифф: потерянные клиенты **и** висящие заказы одним запросом.

```sql
SELECT c.customer_id, o.order_id
FROM customers c
FULL JOIN orders o ON c.customer_id = o.customer_id
WHERE c.customer_id IS NULL OR o.order_id IS NULL;
```

Только «дырки», без успешно связанных пар.

## Если FULL нет (MySQL, старый SQLite)

Собирают из LEFT + RIGHT через `UNION`:

```sql
SELECT c.full_name, o.order_id
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id

UNION

SELECT c.full_name, o.order_id
FROM customers c
RIGHT JOIN orders o ON c.customer_id = o.customer_id;
```

`UNION` (не `UNION ALL`) уберёт дубликаты совпавших строк, которые попали в обе половины.

Эквивалент без RIGHT — второй кусок как LEFT от `orders` к `customers`.

В этой лабе можно писать `FULL JOIN` напрямую.
