<template>
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
      <div v-for="item in tables" :key="item.table" class="tree-node" data-testid="sql-schema-table">
        <div class="tree-row">
          <button
            type="button"
            class="tree-toggle"
            :class="{ 'is-open': isOpen(item.table) }"
            :aria-expanded="isOpen(item.table)"
            :aria-label="`Toggle ${item.table}`"
            @click="$emit('toggle-table', item.table)"
          >
            <SqlGlyph name="chevron" :size="12" />
          </button>
          <button type="button" class="tree-table" @click="$emit('insert-snippet', item.table)">
            <img class="tree-ic" src="/icons/sql/grid.png" alt="" width="14" height="14" />
            {{ item.table }}
          </button>
        </div>
        <div v-show="isOpen(item.table)" class="tree-cols">
          <button
            v-for="column in item.columns"
            :key="column"
            type="button"
            class="tree-col"
            @click="$emit('insert-snippet', `${item.table}.${column}`)"
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
</template>

<script setup>
import SqlGlyph from '@/components/SqlGlyph.vue'

const props = defineProps({
  tables: { type: Array, default: () => [] },
  openTables: { type: Object, default: () => ({}) }
})

defineEmits(['toggle-table', 'insert-snippet'])

function isOpen(name) {
  return props.openTables[name] === true
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
</script>

<style scoped>
.panel-ic,
.tree-ic {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  object-fit: cover;
}

.er-kicker {
  margin: 0 0 8px;
  color: var(--muted);
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
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
  transform: translateY(-50%) rotate(180deg);
  color: var(--primary);
  font-family: var(--mono);
  font-size: 0.62rem;
  writing-mode: vertical-rl;
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
</style>
