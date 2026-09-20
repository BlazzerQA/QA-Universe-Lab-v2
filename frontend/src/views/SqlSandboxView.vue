<template>
  <div class="sql-sandbox-page ui-page" data-testid="sql-sandbox-page">
    <div class="wb">
      <SqlLessonPanel
        :topics="SQL_LESSON_TOPICS"
        :selected-id="selectedLesson"
        :expanded="expandedTopics"
        :lesson="currentLesson"
        :loading="loading"
        @toggle-topic="toggleTopic"
        @select-lesson="selectLesson"
      />

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

      <SqlSchemaPanel
        :tables="schemaTables"
        :open-tables="openTables"
        @toggle-table="toggleTable"
        @insert-snippet="insertSnippet"
      />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import api from '@/api/client'
import { DEFAULT_SQL_LESSON_ID, getSqlLesson, SQL_LESSON_TOPICS } from '@/config/sqlLessons'
import SqlGlyph from '@/components/SqlGlyph.vue'
import SqlLessonPanel from '@/components/SqlLessonPanel.vue'
import SqlMonacoEditor from '@/components/SqlMonacoEditor.vue'
import SqlSchemaPanel from '@/components/SqlSchemaPanel.vue'
import '@/assets/styles/sql-workbench.css'

const selectedLesson = ref(DEFAULT_SQL_LESSON_ID)
const expandedTopics = ref({ join: true })
const currentLesson = computed(() => getSqlLesson(selectedLesson.value))
const sql = ref(getSqlLesson(DEFAULT_SQL_LESSON_ID).sql)
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

function toggleTopic(id) {
  expandedTopics.value = { ...expandedTopics.value, [id]: !expandedTopics.value[id] }
}

function selectLesson(id) {
  selectedLesson.value = id
  sql.value = getSqlLesson(id).sql
  error.value = ''
  result.value = null
  elapsedMs.value = null
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
  grid-template-columns: 264px minmax(0, 1fr) 280px;
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

.toolbar {
  display: flex;
  gap: 6px;
}

.toolbar :deep(.ui-btn) {
  padding: 6px 12px;
  font-size: 0.8rem;
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
