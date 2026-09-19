<template>
  <div class="sql-sandbox-page ui-page" data-testid="sql-sandbox-page">
    <div class="ui-page-body">
      <h1 class="ui-title">{{ t('sql.title') }}</h1>
      <p class="ui-muted intro">
        Пишите SELECT по таблице магазина <code>products</code>. Товары, которые вы добавляете в магазине,
        появляются здесь.
      </p>

      <div class="sandbox-grid">
        <UiCard class="editor-card">
          <h3>Запрос</h3>
          <SqlMonacoEditor v-model="sql" :disabled="loading" @run="runQuery" />
          <div class="button-row">
            <UiButton variant="primary" data-testid="sql-run" :disabled="loading" @click="runQuery">
              Выполнить
            </UiButton>
            <UiButton variant="secondary" data-testid="sql-example" :disabled="loading" @click="loadExample">
              Пример
            </UiButton>
            <UiButton variant="ghost" data-testid="sql-clear" :disabled="loading" @click="clearEditor">
              Очистить
            </UiButton>
          </div>
          <p class="shortcut-hint ui-muted">Ctrl+Enter / ⌘Enter — выполнить</p>
          <p v-if="error" class="ui-hint ui-hint--error" data-testid="sql-error">{{ error }}</p>
        </UiCard>

        <UiCard class="schema-card">
          <h3>Схема</h3>
          <p class="schema-table">{{ schema.table }}</p>
          <ul class="schema-columns">
            <li v-for="column in schema.columns" :key="column"><code>{{ column }}</code></li>
          </ul>
          <p class="ui-muted hint">Только чтение. INSERT/UPDATE/DELETE здесь не выполняются — добавляйте товары в магазине.</p>
        </UiCard>
      </div>

      <UiCard class="result-card">
        <h3>Результат <span v-if="result" class="ui-muted">{{ result.rowCount }} строк</span></h3>
        <p v-if="!result && !error" class="ui-muted" data-testid="sql-result-placeholder">Нажмите «Выполнить», чтобы увидеть таблицу.</p>
        <div v-else-if="result && result.columns.length" class="table-wrap" data-testid="sql-result-table">
          <table>
            <thead>
              <tr>
                <th v-for="column in result.columns" :key="column">{{ column }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, rowIndex) in result.rows" :key="rowIndex">
                <td v-for="(cell, cellIndex) in row" :key="cellIndex">{{ formatCell(cell) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <p v-else-if="result" class="ui-muted">Запрос выполнен, строк нет.</p>
      </UiCard>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import api from '@/api/client'
import SqlMonacoEditor from '@/components/SqlMonacoEditor.vue'
import { useShellI18n } from '@/composables/useShellI18n'

const { t } = useShellI18n()

const EXAMPLE = 'SELECT product_name, price\nFROM products\nORDER BY price DESC;'

const sql = ref('SELECT * FROM products;')
const loading = ref(false)
const error = ref('')
const result = ref(null)
const schema = ref({ table: 'products', columns: ['product_id', 'product_name', 'price'] })

onMounted(async () => {
  try {
    const response = await api.get('/api/sql/schema')
    if (response.data?.table) schema.value = response.data
  } catch {
    /* keep local fallback */
  }
})

async function runQuery() {
  loading.value = true
  error.value = ''
  result.value = null
  try {
    const response = await api.post('/api/sql/query', { sql: sql.value })
    result.value = response.data
  } catch (e) {
    error.value = e.response?.data?.message || e.message || 'Не удалось выполнить запрос.'
  } finally {
    loading.value = false
  }
}

function loadExample() {
  sql.value = EXAMPLE
  error.value = ''
}

function clearEditor() {
  sql.value = ''
  error.value = ''
  result.value = null
}

function formatCell(value) {
  if (value === null || value === undefined) return 'NULL'
  return String(value)
}
</script>

<style scoped>
.intro {
  margin: 0 0 1.25rem;
  max-width: 46rem;
}

.intro code,
.schema-columns code {
  font-family: var(--mono);
  font-size: 0.9em;
}

.sandbox-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.6fr) minmax(220px, 0.8fr);
  gap: 1rem;
  margin-bottom: 1rem;
}

.editor-card h3,
.schema-card h3,
.result-card h3 {
  margin: 0 0 0.75rem;
  color: var(--heading);
}

.button-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.shortcut-hint {
  margin: 0.55rem 0 0;
  font-size: 0.78rem;
}

.schema-table {
  margin: 0 0 0.5rem;
  font-weight: 700;
  color: var(--heading);
  font-family: var(--mono);
}

.schema-columns {
  margin: 0 0 1rem;
  padding-left: 1.1rem;
  color: var(--text);
}

.hint {
  margin: 0;
  font-size: 0.85rem;
}

.result-card h3 .ui-muted {
  font-weight: 500;
  font-size: 0.9rem;
}

.table-wrap {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid var(--border);
  padding: 0.5rem 0.7rem;
  text-align: left;
  font-size: 0.9rem;
}

th {
  background: var(--primary-muted);
  color: var(--heading);
  font-family: var(--mono);
  font-size: 0.8rem;
}

td {
  font-family: var(--mono);
}

@media (max-width: 768px) {
  .sandbox-grid {
    grid-template-columns: 1fr;
  }
}
</style>
