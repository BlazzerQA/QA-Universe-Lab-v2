<template>
  <div class="sql-sandbox-page ui-page" data-testid="sql-sandbox-page">
    <div class="wb">
      <aside class="panel lesson" data-testid="sql-lesson">
        <header class="panel-h">
          <span class="panel-h-left">
            <img class="brand-mark" src="/icons/sql/mark.png" alt="" width="22" height="22" />
            Lesson
          </span>
        </header>
        <div class="panel-body lesson-body">
          <nav class="topic-nav" aria-label="SQL lessons">
            <section v-for="(topic, index) in LESSON_TOPICS" :key="topic.id" class="topic-section">
              <button
                type="button"
                class="topic-toggle"
                :aria-expanded="isTopicOpen(topic.id)"
                :data-testid="`sql-topic-${topic.id}`"
                @click="toggleTopic(topic.id)"
              >
                <span class="topic-chevron" aria-hidden="true">{{ isTopicOpen(topic.id) ? '▾' : '▸' }}</span>
                <img class="topic-logo" :src="topic.logo" alt="" width="18" height="18" />
                <span class="topic-title">{{ index + 1 }}. {{ topic.title }}</span>
                <span class="topic-count">{{ topic.items.length }}</span>
              </button>
              <ul v-if="isTopicOpen(topic.id)" class="lesson-list">
                <li v-for="item in topic.items" :key="item.id">
                  <button
                    type="button"
                    class="lesson-link"
                    :class="{ active: selectedLesson === item.id }"
                    :data-testid="item.testId"
                    :disabled="loading"
                    @click="selectLesson(item.id)"
                  >
                    {{ item.title }}
                  </button>
                </li>
              </ul>
            </section>
          </nav>
          <div class="lesson-meta">
            <p class="rail-label">
              <SqlGlyph name="target" :size="13" />
              Task
            </p>
            <p class="rail-text">{{ currentLesson.task }}</p>
            <p class="rail-label">
              <SqlGlyph name="bulb" :size="13" />
              Hint
            </p>
            <p class="rail-text muted">{{ currentLesson.hint }}</p>
          </div>
        </div>
      </aside>

      <div class="wb-center">
        <section class="panel query">
          <header class="panel-h">
            <span class="panel-h-left">
              <SqlGlyph name="query" :size="14" />
              Query
            </span>
            <div class="toolbar">
              <UiButton variant="primary" data-testid="sql-run" :disabled="loading" @click="runQuery">
                <SqlGlyph name="play" :size="12" />
                Run
              </UiButton>
              <UiButton variant="ghost" data-testid="sql-example" :disabled="loading" @click="loadExample">
                <SqlGlyph name="bulb" :size="13" />
                Example
              </UiButton>
              <UiButton variant="ghost" data-testid="sql-clear" :disabled="loading" @click="clearEditor">
                <SqlGlyph name="trash" :size="13" />
                Clear
              </UiButton>
            </div>
          </header>
          <div class="editor-host">
            <SqlMonacoEditor v-model="sql" height="100%" :disabled="loading" @run="runQuery" />
          </div>
          <p v-if="error" class="ui-hint ui-hint--error editor-error" data-testid="sql-error">{{ error }}</p>
        </section>

        <section class="panel result">
          <header class="panel-h">
            <span class="panel-h-left">
              <SqlGlyph name="result" :size="14" />
              Result
            </span>
            <span v-if="result" class="panel-h-meta">{{ formatRowCount(result.rowCount) }}</span>
          </header>
          <div class="result-body">
            <p v-if="!result && !error" class="empty" data-testid="sql-result-placeholder">
              <img class="empty-art" src="/icons/sql/empty.png" alt="" width="220" height="165" />
              Run a query to see rows.
            </p>
            <div v-else-if="result && result.columns.length" class="table-wrap" data-testid="sql-result-table">
              <table>
                <thead>
                  <tr>
                    <th v-for="column in result.columns" :key="column">{{ column }}</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(row, rowIndex) in result.rows" :key="rowIndex">
                    <td
                      v-for="(cell, cellIndex) in row"
                      :key="cellIndex"
                      :class="{ 'is-null': cell === null || cell === undefined }"
                    >
                      {{ formatCell(cell) }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <p v-else-if="result" class="empty">No rows.</p>
          </div>
          <footer class="status" data-testid="sql-status">
            <SqlGlyph name="check" :size="13" />
            {{ statusText }}
          </footer>
        </section>
      </div>

      <aside class="panel schema">
        <header class="panel-h">
          <span class="panel-h-left">
            <img class="panel-ic" src="/icons/sql/db.png" alt="" width="20" height="20" />
            Schema
          </span>
        </header>
        <div class="panel-body tree">
          <p class="er-kicker">ER Diagram</p>
          <div class="er" data-testid="sql-er">
            <div class="er-cards">
              <div class="er-box er-box--customers">
                <strong>customers</strong>
                <span class="is-key">customer_id (PK)</span>
                <span>full_name</span>
                <span>city</span>
                <span>email</span>
              </div>
              <div class="er-box er-box--orders">
                <strong>orders</strong>
                <span>order_id (PK)</span>
                <span class="is-key">customer_id (FK)</span>
                <span>status</span>
                <span>amount</span>
              </div>
            </div>
            <div class="er-link" aria-hidden="true">
              <span class="er-brace" />
              <span class="er-caption">customer_id</span>
            </div>
          </div>
          <p class="tree-root">Schema Details</p>
          <div v-for="item in schemaTables" :key="item.table" class="tree-node" data-testid="sql-schema-table">
            <div class="tree-row">
              <button
                type="button"
                class="tree-toggle"
                :class="{ 'is-open': isTableOpen(item.table) }"
                :aria-expanded="isTableOpen(item.table)"
                :aria-label="`Toggle ${item.table}`"
                @click="toggleTable(item.table)"
              >
                <SqlGlyph name="chevron" :size="12" />
              </button>
              <button type="button" class="tree-table" @click="insertSnippet(item.table)">
                <img class="tree-ic" src="/icons/sql/grid.png" alt="" width="14" height="14" />
                {{ item.table }}
              </button>
            </div>
            <div v-show="isTableOpen(item.table)" class="tree-cols">
              <button
                v-for="column in item.columns"
                :key="column"
                type="button"
                class="tree-col"
                @click="insertSnippet(`${item.table}.${column}`)"
              >
                {{ column }}
                <span v-if="columnMark(item.table, column)" class="mark">{{
                  columnMark(item.table, column)
                }}</span>
              </button>
            </div>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import api from '@/api/client'
import SqlGlyph from '@/components/SqlGlyph.vue'
import SqlMonacoEditor from '@/components/SqlMonacoEditor.vue'

const LESSON_TOPICS = [
  {
    id: 'select',
    title: 'SELECT',
    logo: '/icons/sql/select.svg',
    items: [
      {
        id: 'select-all',
        testId: 'sql-lesson-select-all',
        title: 'Все колонки',
        task: 'Выведите все товары целиком. 3 строки: Ноутбук, Мышь, Кофе.',
        hint: 'SELECT * читает все колонки. Для разведки схемы это ок, в отчётах лучше перечислять поля.',
        sql: 'SELECT * FROM products;'
      },
      {
        id: 'select-cols',
        testId: 'sql-lesson-select-cols',
        title: 'Нужные поля',
        task: 'Только название и цена товара — без product_id.',
        hint: 'Перечислите колонки через запятую: product_name, price.',
        sql: 'SELECT product_name, price FROM products;'
      },
      {
        id: 'select-order',
        testId: 'sql-lesson-select-order',
        title: 'ORDER BY',
        task: 'Те же поля, но сначала самый дорогой. Ноутбук сверху.',
        hint: 'ORDER BY price DESC. Без DESC SQLite сортирует по возрастанию.',
        sql: 'SELECT product_name, price FROM products ORDER BY price DESC;'
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
        task: 'Только пары клиент–заказ. 3 строки: Анна дважды, Борис один раз.',
        hint: 'Вера и Глеб не попадут — у них нет заказов.',
        sql: 'SELECT c.full_name, o.order_id, o.status, o.amount\nFROM customers c\nINNER JOIN orders o ON c.customer_id = o.customer_id;'
      },
      {
        id: 'left',
        testId: 'sql-join-left',
        title: 'LEFT JOIN',
        task: 'Все клиенты. 5 строк, у Веры и Глеба order_id = NULL.',
        hint: 'Не фильтруйте WHERE o.status — LEFT превратится в INNER.',
        sql: 'SELECT c.full_name, o.order_id, o.status, o.amount\nFROM customers c\nLEFT JOIN orders o ON c.customer_id = o.customer_id\nORDER BY c.full_name;'
      },
      {
        id: 'right',
        testId: 'sql-join-right',
        title: 'RIGHT JOIN',
        task: 'Все заказы. У 104 и 105 имя клиента NULL.',
        hint: 'То же самое: FROM orders LEFT JOIN customers.',
        sql: 'SELECT c.full_name, o.order_id, o.status, o.amount\nFROM customers c\nRIGHT JOIN orders o ON c.customer_id = o.customer_id;'
      },
      {
        id: 'full',
        testId: 'sql-join-full',
        title: 'FULL JOIN',
        task: 'И клиенты без заказов, и заказы без клиента. 7 строк.',
        hint: 'Дырки: WHERE c.customer_id IS NULL OR o.order_id IS NULL.',
        sql: 'SELECT c.full_name, o.order_id, o.status, o.amount\nFROM customers c\nFULL JOIN orders o ON c.customer_id = o.customer_id;'
      },
      {
        id: 'cross',
        testId: 'sql-join-cross',
        title: 'CROSS JOIN',
        task: 'Декарт: 4 клиента × 5 заказов = 20 строк.',
        hint: 'Для связи клиент–заказ это ошибка, не приём.',
        sql: 'SELECT c.full_name, o.order_id\nFROM customers c\nCROSS JOIN orders o;'
      },
      {
        id: 'self',
        testId: 'sql-join-self',
        title: 'SELF JOIN',
        task: 'Пары клиентов из одного города. Анна и Вера — Москва.',
        hint: 'a.customer_id < b.customer_id убирает дубли.',
        sql: 'SELECT a.full_name AS customer_a, b.full_name AS customer_b, a.city\nFROM customers a\nJOIN customers b ON a.city = b.city AND a.customer_id < b.customer_id;'
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
        task: 'Товары дороже 1000 ₽. Ноутбук и Мышь, Кофе не проходит.',
        hint: 'WHERE price > 1000. Сравнение идёт до JOIN-логики, на строках одной таблицы.',
        sql: 'SELECT product_name, price FROM products WHERE price > 1000;'
      },
      {
        id: 'where-like',
        testId: 'sql-lesson-where-like',
        title: 'LIKE',
        task: 'Клиенты, у кого город начинается на «М». Анна и Вера — Москва.',
        hint: 'LIKE \'М%\' — % это любая последовательность символов.',
        sql: "SELECT full_name, city FROM customers WHERE city LIKE 'М%';"
      },
      {
        id: 'where-null',
        testId: 'sql-lesson-where-null',
        title: 'IS NULL',
        task: 'Заказы без клиента. Строки 104 и 105.',
        hint: 'NULL нельзя сравнить через =. Только IS NULL / IS NOT NULL.',
        sql: 'SELECT order_id, status, amount FROM orders WHERE customer_id IS NULL;'
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
        task: 'Сколько всего клиентов. Одна строка со значением 4.',
        hint: 'COUNT(*) считает строки, COUNT(column) пропускает NULL.',
        sql: 'SELECT COUNT(*) AS customers_count FROM customers;'
      },
      {
        id: 'agg-status',
        testId: 'sql-lesson-agg-status',
        title: 'По status',
        task: 'Число заказов в каждом статусе. GROUP BY status.',
        hint: 'В SELECT могут быть только ключ группировки и агрегаты — не full_name без GROUP BY.',
        sql: 'SELECT status, COUNT(*) AS orders_count FROM orders GROUP BY status;'
      }
    ]
  }
]

const lessonItems = LESSON_TOPICS.flatMap((topic) => topic.items)
const selectedLesson = ref('left')
const expandedTopics = ref({ join: true })
const sql = ref(lessonItems.find((item) => item.id === 'left').sql)
const loading = ref(false)
const error = ref('')
const result = ref(null)
const elapsedMs = ref(null)
const openTables = ref({
  products: false,
  customers: true,
  orders: true
})
const schema = ref({
  tables: [
    { table: 'products', columns: ['product_id', 'product_name', 'price'] },
    { table: 'customers', columns: ['customer_id', 'full_name', 'city', 'email'] },
    { table: 'orders', columns: ['order_id', 'customer_id', 'status', 'amount', 'created_at'] }
  ]
})

const schemaTables = computed(() => schema.value?.tables || [])

const currentLesson = computed(
  () => lessonItems.find((lesson) => lesson.id === selectedLesson.value) || lessonItems[4]
)

const statusText = computed(() => {
  const parts = ['read-only']
  if (elapsedMs.value != null) parts.unshift(`${elapsedMs.value} ms`)
  if (result.value) parts.unshift(formatRowCount(result.value.rowCount))
  return parts.join(' · ')
})

onMounted(async () => {
  try {
    const response = await api.get('/api/sql/schema')
    if (response.data?.tables?.length) {
      schema.value = response.data
      const next = { ...openTables.value }
      for (const item of response.data.tables) {
        if (next[item.table] === undefined) next[item.table] = false
      }
      openTables.value = next
    }
  } catch {
    /* keep local fallback */
  }
})

function isTableOpen(name) {
  return openTables.value[name] === true
}

function toggleTable(name) {
  openTables.value = { ...openTables.value, [name]: !isTableOpen(name) }
}

function isTopicOpen(id) {
  return expandedTopics.value[id] === true
}

function toggleTopic(id) {
  expandedTopics.value = { ...expandedTopics.value, [id]: !isTopicOpen(id) }
}

function selectLesson(id) {
  selectedLesson.value = id
  const lesson = lessonItems.find((item) => item.id === id)
  if (lesson) sql.value = lesson.sql
  error.value = ''
  result.value = null
  elapsedMs.value = null
}

function columnMark(table, column) {
  if (
    (table === 'products' && column === 'product_id') ||
    (table === 'customers' && column === 'customer_id') ||
    (table === 'orders' && column === 'order_id')
  ) {
    return 'PK'
  }
  if (table === 'orders' && column === 'customer_id') return 'FK'
  return ''
}

function insertSnippet(text) {
  const current = sql.value || ''
  const glue = current && !/\s$/.test(current) ? ' ' : ''
  sql.value = `${current}${glue}${text}`
}

async function runQuery() {
  loading.value = true
  error.value = ''
  result.value = null
  elapsedMs.value = null
  const started = performance.now()
  try {
    const response = await api.post('/api/sql/query', { sql: sql.value })
    result.value = response.data
    elapsedMs.value = Math.max(1, Math.round(performance.now() - started))
  } catch (e) {
    error.value = e.response?.data?.message || e.message || 'Не удалось выполнить запрос.'
  } finally {
    loading.value = false
  }
}

function loadExample() {
  sql.value = currentLesson.value.sql
  error.value = ''
}

function clearEditor() {
  sql.value = ''
  error.value = ''
  result.value = null
  elapsedMs.value = null
}

function formatRowCount(count) {
  return `${count} rows`
}

function formatCell(value) {
  if (value === null || value === undefined) return 'NULL'
  return String(value)
}
</script>

<style scoped>
.sql-sandbox-page {
  height: calc(100vh - var(--header-h));
  overflow: hidden;
  background: var(--bg);
}

.wb {
  height: 100%;
  padding: 12px;
  display: grid;
  grid-template-columns: 248px minmax(0, 1fr) 280px;
  gap: 10px;
  min-height: 0;
}

.wb-center {
  display: grid;
  grid-template-rows: minmax(0, 1.1fr) minmax(0, 0.9fr);
  gap: 10px;
  min-width: 0;
  min-height: 0;
}

.panel {
  display: flex;
  flex-direction: column;
  min-height: 0;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.panel-h {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  flex-shrink: 0;
  padding: 8px 12px;
  border-bottom: 1px solid var(--border);
  color: var(--muted);
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.panel-h-meta {
  font-family: var(--mono);
  letter-spacing: 0;
  text-transform: none;
  font-weight: 500;
}

.panel-body {
  padding: 12px;
  overflow: auto;
}

.lesson-body {
  display: flex;
  flex-direction: column;
  padding: 0;
  overflow: hidden;
}

.topic-nav {
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: 6px 10px 10px;
}

.topic-section {
  display: flex;
  flex-direction: column;
}

.topic-toggle {
  display: flex;
  align-items: center;
  gap: 7px;
  width: 100%;
  margin: 0;
  padding: 7px 4px;
  border: 0;
  border-bottom: 1px solid var(--border);
  border-radius: 0;
  background: transparent;
  color: var(--primary);
  cursor: pointer;
  text-align: left;
}

.topic-toggle:hover {
  background: var(--primary-muted);
}

.topic-chevron {
  width: 0.9rem;
  flex-shrink: 0;
  font-size: 0.75rem;
  color: var(--muted);
}

.topic-logo {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  object-fit: contain;
  background: transparent;
}

.topic-title {
  flex: 1;
  min-width: 0;
  font-size: 0.78rem;
  font-weight: 700;
  letter-spacing: 0.06em;
}

.topic-count {
  font-size: 0.72rem;
  font-weight: 700;
  color: var(--muted);
  font-variant-numeric: tabular-nums;
}

.lesson-list {
  list-style: none;
  margin: 0.1rem 0 0.35rem;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.lesson-list li + li .lesson-link {
  border-top: 1px solid var(--border);
}

.lesson-link {
  display: block;
  width: 100%;
  padding: 8px 10px 8px 12px;
  border: 0;
  border-left: 2px solid transparent;
  border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
  background: none;
  color: var(--text);
  font-size: 0.82rem;
  line-height: 1.35;
  text-align: left;
  cursor: pointer;
}

.lesson-link:hover:not(:disabled),
.lesson-link:focus-visible {
  background: var(--primary-muted);
  color: var(--primary);
  border-left-color: var(--primary);
}

.lesson-link.active {
  background: var(--primary-muted);
  color: var(--primary);
  font-weight: 600;
  border-left-color: var(--primary);
}

.lesson-meta {
  flex-shrink: 0;
  padding: 10px 12px 12px;
  border-top: 1px solid var(--border);
}

.toolbar {
  display: flex;
  gap: 6px;
}

.toolbar :deep(.ui-btn) {
  padding: 6px 12px;
  font-size: 0.8rem;
}

.panel-h-left {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.brand-mark {
  width: 22px;
  height: 22px;
  border-radius: 6px;
  object-fit: cover;
  border: 1px solid var(--border);
}

.rail-label {
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 0 0 6px;
  color: var(--muted);
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.rail-label:not(:first-child) {
  margin-top: 12px;
}

.rail-text {
  margin: 0;
  color: var(--text);
  font-size: 0.82rem;
  line-height: 1.4;
}

.rail-text.muted {
  color: var(--muted);
}

.editor-host {
  flex: 1;
  min-height: 0;
}

.editor-host :deep(.qa-monaco) {
  height: 100% !important;
  margin: 0;
  border: 0;
  border-radius: 0;
}

.editor-error {
  margin: 0;
  padding: 8px 12px;
  border-top: 1px solid var(--border);
}

.result-body {
  flex: 1;
  min-height: 0;
  overflow: auto;
}

.empty-art {
  width: min(240px, 82%);
  height: auto;
  object-fit: contain;
  background: transparent;
}

.panel-ic,
.tree-ic {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  object-fit: cover;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin: 0;
  padding: 16px 12px;
  color: var(--muted);
  font-size: 0.85rem;
  min-height: 100%;
}

.er-kicker {
  margin: 0 0 8px;
  color: var(--muted);
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.status {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-shrink: 0;
  padding: 6px 12px;
  border-top: 1px solid var(--border);
  color: var(--success);
  font-family: var(--mono);
  font-size: 0.72rem;
}

.er {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 72px;
  gap: 4px;
  align-items: stretch;
  margin-bottom: 14px;
}

.er-cards {
  display: flex;
  flex-direction: column;
  gap: 12px;
  min-width: 0;
}

.er-box {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: 8px 9px;
  border: 1px solid var(--border-strong);
  border-radius: var(--radius-sm);
  background: var(--input-bg);
  font-family: var(--mono);
  font-size: 0.68rem;
  color: var(--muted);
}

.er-box strong {
  color: var(--heading);
  margin-bottom: 2px;
}

.er-box .is-key {
  color: var(--primary);
}

.er-box--customers {
  border-color: var(--primary);
}

.er-box--orders {
  border-color: #8b5cf6;
}

.er-link {
  position: relative;
  min-height: 100%;
}

.er-brace {
  position: absolute;
  left: 0;
  top: 22%;
  bottom: 22%;
  width: 16px;
  border: 2px solid var(--primary);
  border-left: 0;
  border-radius: 0 8px 8px 0;
}

.er-brace::before,
.er-brace::after {
  content: '';
  position: absolute;
  left: -5px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  border: 2px solid var(--primary);
  background: var(--card);
}

.er-brace::before {
  top: -5px;
}

.er-brace::after {
  bottom: -5px;
}

.er-caption {
  position: absolute;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--primary);
  font-family: var(--mono);
  font-size: 0.62rem;
  writing-mode: vertical-rl;
  transform: translateY(-50%) rotate(180deg);
  letter-spacing: 0.04em;
}

.tree-root {
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 14px 0 8px;
  color: var(--muted);
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.08em;
}

.tree-node + .tree-node {
  margin-top: 2px;
}

.tree-row {
  display: flex;
  align-items: center;
  gap: 2px;
}

.tree-toggle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 22px;
  padding: 0;
  border: 0;
  background: none;
  color: var(--muted);
  cursor: pointer;
}

.tree-toggle:hover {
  color: var(--heading);
}

.tree-toggle :deep(.sql-glyph) {
  transition: transform 0.15s ease;
}

.tree-toggle.is-open :deep(.sql-glyph) {
  transform: rotate(90deg);
}

.tree-table,
.tree-col {
  display: flex;
  align-items: center;
  gap: 6px;
  width: 100%;
  padding: 3px 0;
  border: 0;
  background: none;
  color: var(--text);
  font-family: var(--mono);
  font-size: 0.78rem;
  text-align: left;
  cursor: pointer;
}

.tree-table {
  margin-top: 0;
  color: var(--heading);
  font-weight: 700;
}

.tree-col {
  padding-left: 32px;
  color: var(--muted);
}

.tree-table:hover,
.tree-col:hover {
  color: var(--primary);
}

.mark {
  margin-left: 6px;
  color: var(--primary);
  font-size: 0.65rem;
  font-weight: 700;
}

.table-wrap {
  overflow: auto;
  height: 100%;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border-bottom: 1px solid var(--border);
  padding: 7px 10px;
  text-align: left;
  font-size: 0.8rem;
  white-space: nowrap;
}

th {
  position: sticky;
  top: 0;
  background: var(--panel);
  color: var(--muted);
  font-family: var(--mono);
  font-size: 0.72rem;
  font-weight: 600;
}

td {
  font-family: var(--mono);
  color: var(--text);
}

td.is-null {
  color: var(--muted);
  font-style: italic;
}

tbody tr:hover td {
  background: var(--primary-muted);
}

@media (max-width: 1100px) {
  .sql-sandbox-page {
    height: auto;
    overflow: visible;
  }

  .wb {
    grid-template-columns: 1fr;
    height: auto;
  }

  .wb-center {
    grid-template-rows: 320px 280px;
  }
}
</style>
