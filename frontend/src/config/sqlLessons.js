import { annotatedSql } from '@/sql/annotatedSql'

export const DEFAULT_SQL_LESSON_ID = 'left'

export const SQL_LESSON_TOPICS = [
  {
    id: 'select',
    title: 'SELECT',
    logo: '/icons/sql/select.svg',
    items: [
      {
        id: 'select-all',
        testId: 'sql-lesson-select-all',
        title: 'Все колонки',
        tasks: [
          'Выведите все товары целиком. 3 строки: Ноутбук, Мышь, Кофе.',
          'Посчитайте глазами колонки результата: product_id, product_name, price.',
          'Сравните * с явным списком полей — результат тот же, но схема видна сразу.'
        ],
        hints: [
          'SELECT * читает все колонки. Для разведки схемы это ок, в отчётах лучше перечислять поля.',
          '* — это «все столбцы», не «все строки». Строки режет WHERE.',
          'Читается как: выбрать все столбцы из таблицы products.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT *', note: 'какие столбцы показать' },
            { code: 'FROM products;', note: 'из какой таблицы' }
          ],
          'выбрать все столбцы из таблицы products'
        )
      },
      {
        id: 'select-cols',
        testId: 'sql-lesson-select-cols',
        title: 'Нужные поля',
        tasks: [
          'Только название и цена товара — без product_id.',
          'Поменяйте порядок: сначала price, потом product_name.',
          'Добавьте product_id обратно и сравните, какие колонки лишние для витрины.'
        ],
        hints: [
          'Перечислите колонки через запятую: product_name, price.',
          'Порядок в SELECT = порядок столбцов в Result, не в таблице.',
          'Читается как: выбрать название и цену из таблицы products.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT product_name, price', note: 'какие столбцы показать' },
            { code: 'FROM products;', note: 'из какой таблицы' }
          ],
          'выбрать название и цену из таблицы products'
        )
      },
      {
        id: 'select-order',
        testId: 'sql-lesson-select-order',
        title: 'ORDER BY',
        tasks: [
          'Те же поля, но сначала самый дорогой. Ноутбук сверху.',
          'Разверните сортировку в ASC — Кофе должен оказаться первым.',
          'Отсортируйте по имени: ORDER BY product_name.'
        ],
        hints: [
          'ORDER BY price DESC. Без DESC SQLite сортирует по возрастанию.',
          'ASC — по возрастанию, DESC — по убыванию. Пишут в конце запроса.',
          'Читается как: выбрать название и цену из products и отсортировать по убыванию цены.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT product_name, price', note: 'какие столбцы показать' },
            { code: 'FROM products', note: 'из какой таблицы' },
            { code: 'ORDER BY price DESC;', note: 'сортировать по цене, сначала большие' }
          ],
          'выбрать название и цену из products, отсортировать по убыванию цены'
        )
      }
    ]
  },
  {
    id: 'join',
    title: 'JOIN',
    logo: '/icons/sql/join.png',
    items: [
      {
        id: 'inner',
        testId: 'sql-join-inner',
        title: 'INNER JOIN',
        tasks: [
          'Только пары клиент–заказ. 3 строки: Анна дважды, Борис один раз.',
          'Убедитесь, что Веры и Глеба в результате нет.',
          'Добавьте o.amount и найдите самый большой заказ среди пар.'
        ],
        hints: [
          'Вера и Глеб не попадут — у них нет заказов.',
          'INNER оставляет только пересечение: есть и клиент, и заказ.',
          'Читается как: выбрать имя и заказ там, где клиент и заказ совпали по customer_id.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT c.full_name, o.order_id, o.status, o.amount', note: 'какие столбцы показать' },
            { code: 'FROM customers c', note: 'левая таблица' },
            { code: 'INNER JOIN orders o', note: 'только совпавшие заказы' },
            { code: '  ON c.customer_id = o.customer_id;', note: 'условие связи' }
          ],
          'выбрать имя и заказ там, где клиент и заказ совпали по customer_id'
        )
      },
      {
        id: 'left',
        testId: 'sql-join-left',
        title: 'LEFT JOIN',
        tasks: [
          'Все клиенты. 5 строк, у Веры и Глеба order_id = NULL.',
          'Отфильтруйте клиентов без заказов: WHERE o.order_id IS NULL.',
          'Посмотрите, сколько раз встречается Анна — у неё два заказа.'
        ],
        hints: [
          'Не фильтруйте WHERE o.status — LEFT превратится в INNER.',
          'LEFT = все строки левой таблицы. Дырки справа заполняются NULL.',
          'Читается как: взять всех customers и слева приклеить orders по customer_id.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT c.full_name, o.order_id, o.status, o.amount', note: 'какие столбцы показать' },
            { code: 'FROM customers c', note: 'левая таблица — её строки не теряем' },
            { code: 'LEFT JOIN orders o', note: 'приклеить заказы, даже если их нет' },
            { code: '  ON c.customer_id = o.customer_id', note: 'условие связи' },
            { code: 'ORDER BY c.full_name;', note: 'сортировка по имени' }
          ],
          'взять всех customers и слева приклеить orders по customer_id'
        )
      },
      {
        id: 'right',
        testId: 'sql-join-right',
        title: 'RIGHT JOIN',
        tasks: [
          'Все заказы. У 104 и 105 имя клиента NULL.',
          'Найдите заказы-сироты: WHERE c.customer_id IS NULL.',
          'Перепишите тот же смысл через FROM orders LEFT JOIN customers.'
        ],
        hints: [
          'То же самое: FROM orders LEFT JOIN customers.',
          'RIGHT = все строки правой таблицы. Здесь правая — orders.',
          'Читается как: взять все orders и сохранить их, даже если клиента нет.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT c.full_name, o.order_id, o.status, o.amount', note: 'какие столбцы показать' },
            { code: 'FROM customers c', note: 'левая таблица' },
            { code: 'RIGHT JOIN orders o', note: 'правая таблица — её строки не теряем' },
            { code: '  ON c.customer_id = o.customer_id;', note: 'условие связи' }
          ],
          'взять все orders и сохранить их, даже если клиента нет'
        )
      },
      {
        id: 'full',
        testId: 'sql-join-full',
        title: 'FULL JOIN',
        tasks: [
          'И клиенты без заказов, и заказы без клиента. 7 строк.',
          'Оставьте только дырки: WHERE c.customer_id IS NULL OR o.order_id IS NULL.',
          'Сверьте: 4 клиента + 5 заказов − 2 совпавшие пары Анны и 1 Бориса = 7.'
        ],
        hints: [
          'Дырки: WHERE c.customer_id IS NULL OR o.order_id IS NULL.',
          'FULL = LEFT ∪ RIGHT. SQLite 3.39+ умеет FULL JOIN.',
          'Читается как: склеить customers и orders по id, не теряя ни одну сторону.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT c.full_name, o.order_id, o.status, o.amount', note: 'какие столбцы показать' },
            { code: 'FROM customers c', note: 'одна сторона' },
            { code: 'FULL JOIN orders o', note: 'обе стороны целиком' },
            { code: '  ON c.customer_id = o.customer_id;', note: 'условие связи' }
          ],
          'склеить customers и orders по id, не теряя ни одну сторону'
        )
      },
      {
        id: 'cross',
        testId: 'sql-join-cross',
        title: 'CROSS JOIN',
        tasks: [
          'Декарт: 4 клиента × 5 заказов = 20 строк.',
          'Добавьте WHERE c.customer_id = o.customer_id — получится почти INNER.',
          'Попробуйте CROSS JOIN products: строк станет 4 × 3 = 12, если убрать orders.'
        ],
        hints: [
          'Для связи клиент–заказ это ошибка, не приём.',
          'CROSS не имеет ON: каждая строка × каждая строка.',
          'Читается как: взять каждую пару «клиент × заказ» без условия связи.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT c.full_name, o.order_id', note: 'какие столбцы показать' },
            { code: 'FROM customers c', note: 'каждая строка слева' },
            { code: 'CROSS JOIN orders o;', note: 'умножить на каждую строку справа' }
          ],
          'взять каждую пару «клиент × заказ» без условия связи'
        )
      },
      {
        id: 'self',
        testId: 'sql-join-self',
        title: 'SELF JOIN',
        tasks: [
          'Пары клиентов из одного города. Анна и Вера — Москва.',
          'Уберите `a.customer_id < b.customer_id` и посмотрите дубли (Анна–Вера и Вера–Анна).',
          'Сделайте пары по email-домену или просто выведите всех из Казани.'
        ],
        hints: [
          'a.customer_id < b.customer_id убирает дубли.',
          'SELF JOIN — это JOIN таблицы самой с собой через два алиаса.',
          'Читается как: взять customers дважды и найти пары с одинаковым городом.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT a.full_name AS customer_a,', note: 'первый клиент' },
            { code: '       b.full_name AS customer_b,', note: 'второй клиент' },
            { code: '       a.city', note: 'общий город' },
            { code: 'FROM customers a', note: 'таблица в роли A' },
            { code: 'JOIN customers b', note: 'та же таблица в роли B' },
            { code: '  ON a.city = b.city', note: 'связать по городу' },
            { code: ' AND a.customer_id < b.customer_id;', note: 'без зеркальных дублей' }
          ],
          'взять customers дважды и найти пары с одинаковым городом'
        )
      }
    ]
  },
  {
    id: 'where',
    title: 'WHERE',
    logo: '/icons/sql/where.svg',
    items: [
      {
        id: 'where-cmp',
        testId: 'sql-lesson-where-cmp',
        title: 'Сравнение',
        tasks: [
          'Товары дороже 1000 ₽. Ноутбук и Мышь, Кофе не проходит.',
          'Сделайте price >= 1290 — должна остаться Мышь и Ноутбук.',
          'Инверсия: WHERE price < 1000. Останется только Кофе.'
        ],
        hints: [
          'WHERE price > 1000. Сравнение идёт на строках одной таблицы.',
          'Операторы: = != <> > < >= <=. Строки сравнивайте в кавычках.',
          'Читается как: выбрать название и цену из products, где цена больше 1000.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT product_name, price', note: 'какие столбцы показать' },
            { code: 'FROM products', note: 'из какой таблицы' },
            { code: 'WHERE price > 1000;', note: 'оставить только строки с ценой > 1000' }
          ],
          'выбрать название и цену из products, где цена больше 1000'
        )
      },
      {
        id: 'where-like',
        testId: 'sql-lesson-where-like',
        title: 'LIKE',
        tasks: [
          'Клиенты, у кого город начинается на «М». Анна и Вера — Москва.',
          'Найдите имена на «А»: WHERE full_name LIKE \'А%\'.',
          'Попробуйте \'%а%\' — вхождение буквы в любом месте.'
        ],
        hints: [
          'LIKE \'М%\' — % это любая последовательность символов.',
          '_ — ровно один символ. LIKE чувствителен к регистру в SQLite по умолчанию.',
          'Читается как: выбрать имя и город из customers, где город начинается на М.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT full_name, city', note: 'какие столбцы показать' },
            { code: 'FROM customers', note: 'из какой таблицы' },
            { code: "WHERE city LIKE 'М%';", note: 'шаблон: М + что угодно дальше' }
          ],
          'выбрать имя и город из customers, где город начинается на М'
        )
      },
      {
        id: 'where-null',
        testId: 'sql-lesson-where-null',
        title: 'IS NULL',
        tasks: [
          'Заказы без клиента. Строки 104 и 105.',
          'Противоположность: WHERE customer_id IS NOT NULL — заказы с хозяином.',
          'Сравните = NULL: такое условие не вернёт строк.'
        ],
        hints: [
          'NULL нельзя сравнить через =. Только IS NULL / IS NOT NULL.',
          'NULL значит «значение неизвестно», а не 0 и не пустая строка.',
          'Читается как: выбрать заказы из orders, у которых нет customer_id.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT order_id, status, amount', note: 'какие столбцы показать' },
            { code: 'FROM orders', note: 'из какой таблицы' },
            { code: 'WHERE customer_id IS NULL;', note: 'только строки без клиента' }
          ],
          'выбрать заказы из orders, у которых нет customer_id'
        )
      }
    ]
  },
  {
    id: 'agg',
    title: 'GROUP BY',
    logo: '/icons/sql/agg.svg',
    items: [
      {
        id: 'agg-count',
        testId: 'sql-lesson-agg-count',
        title: 'COUNT',
        tasks: [
          'Сколько всего клиентов. Одна строка со значением 4.',
          'COUNT(email) vs COUNT(*): если бы email был NULL, цифры разошлись бы.',
          'Посчитайте заказы: COUNT(*) FROM orders — должно быть 5.'
        ],
        hints: [
          'COUNT(*) считает строки, COUNT(column) пропускает NULL.',
          'AS customers_count даёт столбцу понятное имя в Result.',
          'Читается как: посчитать число строк в таблице customers.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT COUNT(*) AS customers_count', note: 'агрегат: сколько строк' },
            { code: 'FROM customers;', note: 'в какой таблице' }
          ],
          'посчитать число строк в таблице customers'
        )
      },
      {
        id: 'agg-status',
        testId: 'sql-lesson-agg-status',
        title: 'По status',
        tasks: [
          'Число заказов в каждом статусе. GROUP BY status.',
          'Добавьте SUM(amount) — сумма денег по статусу.',
          'HAVING COUNT(*) > 1 оставит только статусы с несколькими заказами.'
        ],
        hints: [
          'В SELECT могут быть только ключ группировки и агрегаты — не full_name без GROUP BY.',
          'WHERE фильтрует строки до группы, HAVING — уже после GROUP BY.',
          'Читается как: сгруппировать заказы по status и посчитать, сколько в каждой кучке.'
        ],
        sql: annotatedSql(
          [
            { code: 'SELECT status, COUNT(*) AS orders_count', note: 'ключ группы + агрегат' },
            { code: 'FROM orders', note: 'из какой таблицы' },
            { code: 'GROUP BY status;', note: 'разрезать строки на кучки по status' }
          ],
          'сгруппировать заказы по status и посчитать, сколько в каждой кучке'
        )
      }
    ]
  }
]

export const SQL_LESSON_ITEMS = SQL_LESSON_TOPICS.flatMap((topic) => topic.items)

export function getSqlLesson(id) {
  return (
    SQL_LESSON_ITEMS.find((item) => item.id === id) ||
    SQL_LESSON_ITEMS.find((item) => item.id === DEFAULT_SQL_LESSON_ID)
  )
}
