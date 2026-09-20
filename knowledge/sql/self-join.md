# SELF JOIN

Self join — это не отдельный вид в SQL, а приём: **одна и та же таблица** стоит слева и справа. Без разных алиасов СУБД не поймёт, какую «копию» ты имеешь в виду.

## Зачем

Сравнить строки внутри одной таблицы:

- сотрудник и его руководитель (`manager_id` → `employee_id`);
- клиенты из одного города;
- заказ и «предыдущий» заказ того же клиента.

## Клиенты из одного города

```sql
SELECT a.full_name AS customer_a,
       b.full_name AS customer_b,
       a.city
FROM customers a
JOIN customers b ON a.city = b.city
               AND a.customer_id < b.customer_id;
```

`a.customer_id < b.customer_id` убирает:

- строку человека с самим собой (`Анна–Анна`);
- зеркальный дубль (`Анна–Вера` и `Вера–Анна`).

Это обычный `INNER JOIN` таблицы на себя. Можно `LEFT`, если нужны и те, у кого нет «пары» в городе.

## Иерархия (классика собеса)

Таблица `employees(employee_id, name, manager_id)`:

```sql
SELECT e.name AS employee,
       m.name AS manager
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.employee_id;
```

`LEFT`, потому что у директора `manager_id` пустой — он всё равно должен быть в списке.

## Заказы одного клиента рядом

```sql
SELECT a.order_id AS first_order,
       b.order_id AS second_order,
       a.customer_id,
       a.amount AS amount_a,
       b.amount AS amount_b
FROM orders a
JOIN orders b ON a.customer_id = b.customer_id
             AND a.order_id < b.order_id;
```

Пары заказов одного покупателя. У заказа без `customer_id` пары не будет (`NULL` в JOIN не равен `NULL`).

## Правило

Два алиаса обязательны. Колонки всегда с префиксом (`a.city`, `b.city`), иначе неоднозначность.
