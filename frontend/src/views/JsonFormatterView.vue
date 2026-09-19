<template>
  <div class="json-formatter-page ui-page">
    <UiFloatingBg />

    <div class="container ui-page-body">
      <h1 class="ui-title">📦 JSON Formatter & Tree Viewer</h1>
      <div class="editor-area" :class="{ 'left-collapsed': isInputCollapsed }">
        <UiCard class="input-panel" :class="{ collapsed: isInputCollapsed }">
          <h3>✏️ Введите сырой JSON</h3>
          <MonacoEditor
            ref="jsonEditor"
            v-model="jsonInput"
            language="json"
            height="400px"
            test-id="json-input"
            aria-label="JSON input editor"
            @run="formatJson"
          />
          <div class="button-group">
            <UiButton variant="primary" data-testid="json-format" @click="formatJson">🎨 Отформатировать</UiButton>
            <UiButton variant="danger" data-testid="json-clear" @click="clearJson">🗑 Очистить</UiButton>
            <UiButton variant="primary" data-testid="json-example" @click="loadExample">📋 Пример</UiButton>
          </div>
          <p class="shortcut-hint ui-muted">Ctrl+Enter / ⌘Enter — отформатировать</p>
          <div v-if="errorMsg" class="ui-hint ui-hint--error" data-testid="json-error">{{ errorMsg }}</div>
        </UiCard>
        <UiCard class="output-panel">
          <div class="output-header">
            <h3>🌳 Древовидная структура</h3>
            <UiButton
              variant="ghost"
              class="toggle-panel-btn"
              :class="{ fixed: isInputCollapsed }"
              :title="isInputCollapsed ? 'Развернуть панель' : 'Свернуть панель'"
              @click="toggleInputPanel"
            >
              {{ isInputCollapsed ? '▶ Показать редактор JSON' : '◀ Скрыть редактор JSON' }}
            </UiButton>
          </div>
          <div class="json-viewer" data-testid="json-tree">
            <JsonTreeNode v-if="parsedJson !== null" :data="parsedJson" />
            <i v-else class="ui-muted">Здесь появится дерево после форматирования</i>
          </div>
        </UiCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import { nextTick, ref } from 'vue'
import JsonTreeNode from '@/components/JsonTreeNode.vue'
import MonacoEditor from '@/components/MonacoEditor.vue'

const jsonInput = ref('')
const parsedJson = ref(null)
const errorMsg = ref('')
const isInputCollapsed = ref(false)
const jsonEditor = ref(null)

function formatJson() {
  errorMsg.value = ''
  try {
    const obj = JSON.parse(jsonInput.value)
    jsonInput.value = JSON.stringify(obj, null, 2)
    parsedJson.value = obj
  } catch (e) {
    errorMsg.value = e.message
    parsedJson.value = null
  }
}

function clearJson() {
  jsonInput.value = ''
  parsedJson.value = null
  errorMsg.value = ''
}

function loadExample() {
  const example = {
    "universe": "QA Universe",
    "version": 1.0,
    "features": ["API Testing", "UI Testing", "JSON Formatter"],
    "config": { "darkMode": true, "timeout": 30, "plugins": ["RestAssured", "Selenide"] },
    "nullValue": null,
    "isActive": true
  }
  jsonInput.value = JSON.stringify(example, null, 2)
  parsedJson.value = example
  errorMsg.value = ''
}

function toggleInputPanel() {
  isInputCollapsed.value = !isInputCollapsed.value
  if (!isInputCollapsed.value) {
    nextTick(() => jsonEditor.value?.layout())
  }
}
</script>

<style scoped>
.container {
  max-width: 1400px;
}

.editor-area {
  display: flex;
  gap: 20px;
  margin-top: 24px;
  flex-wrap: wrap;
}

.input-panel,
.output-panel {
  flex: 1;
  min-width: 300px;
}

.input-panel.collapsed {
  display: none;
}

.editor-area.left-collapsed .output-panel {
  flex: 1;
  min-width: 100%;
}

.output-panel {
  overflow: auto;
  max-height: 70vh;
}

.button-group {
  margin-top: 15px;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.shortcut-hint {
  margin: 0.55rem 0 0;
  font-size: 0.78rem;
}

.json-viewer {
  font-family: var(--mono);
  font-size: 14px;
}

h3 {
  margin-top: 0;
  color: var(--heading);
  font-size: 1.2rem;
}

.output-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.toggle-panel-btn.fixed {
  position: fixed;
  top: 100px;
  right: 20px;
  z-index: 1000;
}

@media (max-width: 768px) {
  .editor-area {
    flex-direction: column;
  }
  .toggle-panel-btn.fixed {
    top: 90px;
    right: 10px;
  }
}
</style>
