# RIGHT JOIN

`RIGHT JOIN` — зеркало `LEFT JOIN`: в результате **все строки правой** таблицы. Нет пары слева — колонки левой таблицы `NULL`.

## Синтаксис

```sql
SELECT c.full_name, o.order_id, o.amount
FROM customers c
RIGHT JOIN orders o ON c.customer_id = o.customer_id;
```

Заказ без клиента останется в выборке, `full_name` будет `NULL`. Клиент без заказов — не попадёт.

## Как думать

| customers (слева) | orders (справа) | RIGHT |
|---|---|---|
| Анна | заказ Анны | Анна + заказ |
| Вера | нет заказа | нет |
| — | заказ без клиента | NULL + заказ |

## Зачем он нужен

На практике чаще пишут `LEFT JOIN` и **меняют таблицы местами** — читается одинаково во всех СУБД и привычнее:

```sql
-- то же, что RIGHT JOIN от customers к orders
SELECT c.full_name, o.order_id, o.amount
FROM orders o
LEFT JOIN customers c ON c.customer_id = o.customer_id;
```

Запомни оба: на собесе могут показать `RIGHT` и спросить, кто «главный».

## QA-сценарий

Заказы без покупателя — битые данные (удалили клиента, `customer_id` не проставили, внешний ключ не стоит):

```sql
SELECT o.order_id, o.amount, o.status
FROM customers c
RIGHT JOIN orders o ON c.customer_id = o.customer_id
WHERE c.customer_id IS NULL;
```

Или проще через LEFT от `orders`.

## SQLite

В этой лабе SQLite 3.46 — `RIGHT JOIN` работает. В старых версиях (до 3.39) его не было, только эмуляция через `LEFT` с перестановкой таблиц.
