<template>
  <div class="json-tree-node">
    <div class="node-content" @mouseenter="hovered = true" @mouseleave="hovered = false">
      <!-- Индекс для массива -->
      <span v-if="arrayIndex !== null" class="array-index">{{ arrayIndex }}:</span>

      <!-- Ключ для объекта -->
      <template v-if="keyName !== null">
        <span class="key-name">{{ keyName }}:</span>
      </template>

      <!-- примитивы -->
      <template v-if="isNull">
        <span class="value-null">null</span>
        <button class="copy-btn" @click.stop="copyValue('null')">{{ copyLabel }}</button>
      </template>
      <template v-else-if="isPrimitive">
        <span :class="valueClass">{{ displayValue }}</span>
        <button class="copy-btn" @click.stop="copyValue(String(rawValue))">{{ copyLabel }}</button>
      </template>

      <!-- объект или массив -->
      <template v-else>
        <span
          v-if="hasChildren"
          class="toggle-icon"
          @click.stop="expanded = !expanded"
        >
          {{ expanded ? '▼ ' : '▶ ' }}
        </span>
        <span v-else class="toggle-icon">  </span>
        <span class="bracket">{{ isArray ? '[' : '{' }}</span>
        <span v-if="hasChildren && !expanded" class="ellipsis"> … </span>
        <span class="bracket">{{ isArray ? ']' : '}' }}</span>
      </template>
    </div>

    <!-- дети -->
    <div
      v-if="hasChildren && expanded"
      class="children-container"
    >
      <JsonTreeNode
        v-for="(childValue, childKey) in children"
        :key="childKey"
        :data="childValue"
        :key-name="isArray ? null : childKey"
        :array-index="isArray ? childKey : null"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  data: { type: null, required: true },
  keyName: { type: [String, Number, null], default: null },
  arrayIndex: { type: Number, default: null }
})

const expanded = ref(true)
const hovered = ref(false)
const copyLabel = ref('📋')

const isNull = computed(() => props.data === null)
const isPrimitive = computed(() => typeof props.data !== 'object')
const isArray = computed(() => Array.isArray(props.data))
const hasChildren = computed(() => {
  if (props.data === null || typeof props.data !== 'object') return false
  return Object.keys(props.data).length > 0
})
const children = computed(() => {
  if (props.data === null || typeof props.data !== 'object') return []
  return props.data
})

const rawValue = computed(() => props.data)

const displayValue = computed(() => {
  const v = props.data
  if (typeof v === 'string') return `"${v}"`
  return String(v)
})

const valueClass = computed(() => {
  const v = props.data
  if (typeof v === 'string') return 'value-string'
  if (typeof v === 'number') return 'value-number'
  if (typeof v === 'boolean') return 'value-boolean'
  return ''
})

function copyValue(text) {
  navigator.clipboard.writeText(text).then(() => {
    copyLabel.value = '✓'
    setTimeout(() => { copyLabel.value = '📋' }, 800)
  }).catch(() => alert('Не удалось скопировать'))
}
</script>

<style scoped>
.json-tree-node {
  margin-left: 0;
  padding: 2px 0;
  position: relative;
}

.node-content {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  border-radius: 4px;
  transition: background 0.1s;
  padding: 2px 4px;
}

.node-content:hover {
  background-color: var(--card-hover);
}

.toggle-icon {
  cursor: pointer;
  user-select: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  font-size: 14px;
  flex-shrink: 0;
}

.array-index {
  color: var(--syntax-muted);
  min-width: 30px;
  flex-shrink: 0;
}

.key-name {
  color: var(--syntax-key);
  font-weight: bold;
  margin-right: 6px;
  flex-shrink: 0;
}

.value-string { color: var(--syntax-string); }
.value-number { color: var(--syntax-number); }
.value-boolean { color: var(--syntax-boolean); }
.value-null { color: var(--syntax-boolean); font-style: italic; }
.bracket,
.ellipsis {
  color: var(--syntax-muted);
  margin: 0 2px;
}

.copy-btn {
  opacity: 0;
  background: var(--panel);
  border: 1px solid var(--border);
  color: var(--muted);
  border-radius: 3px;
  cursor: pointer;
  font-size: 11px;
  padding: 2px 6px;
  margin-left: 8px;
  transition: opacity 0.1s;
  width: auto;
}

.node-content:hover .copy-btn {
  opacity: 1;
}

.copy-btn:hover {
  background: var(--primary);
  color: var(--primary-text);
  border-color: var(--primary);
}

.children-container {
  margin-left: 24px;
  border-left: 1px dashed var(--border);
}
</style>
