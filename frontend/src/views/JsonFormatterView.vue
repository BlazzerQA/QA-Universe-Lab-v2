<template>
  <div class="json-formatter-page">
    <div class="floating-background" ref="bgContainer"></div>

    <div class="container">
      <div>
        <h1 class="page-title">📦 JSON Formatter & Tree Viewer</h1>
      </div>
      <div class="editor-area" :class="{ 'left-collapsed': isInputCollapsed }">
        <div class="input-panel" :class="{ collapsed: isInputCollapsed }">
          <h3>✏️ Введите сырой JSON</h3>
          <textarea
            v-model="jsonInput"
            placeholder='Например: { "name": "QA", "tools": ["Postman", "RestAssured"] }'
            data-testid="json-input"
          ></textarea>
          <div class="button-group">
            <button class="btn btn-primary" data-testid="json-format" @click="formatJson">🎨 Отформатировать</button>
            <button class="btn btn-danger" data-testid="json-clear" @click="clearJson">🗑 Очистить</button>
            <button class="btn btn-primary" data-testid="json-example" @click="loadExample">📋 Пример</button>
          </div>
          <div v-if="errorMsg" class="error-message" data-testid="json-error">{{ errorMsg }}</div>
        </div>
        <div class="output-panel">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <h3>🌳 Древовидная структура</h3>
            <button
              class="toggle-panel-btn"
              :class="{ fixed: isInputCollapsed }"
              :title="isInputCollapsed ? 'Развернуть панель' : 'Свернуть панель'"
              @click="toggleInputPanel"
            >
              {{ isInputCollapsed ? '▶ Показать редактор JSON' : '◀ Скрыть редактор JSON' }}
            </button>
          </div>
          <div class="json-viewer" data-testid="json-tree">
            <JsonTreeNode v-if="parsedJson !== null" :data="parsedJson" />
            <i v-else style="color: #888;">Здесь появится дерево после форматирования</i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import JsonTreeNode from '@/components/JsonTreeNode.vue'

const jsonInput = ref('')
const parsedJson = ref(null)
const errorMsg = ref('')
const isInputCollapsed = ref(false)
const bgContainer = ref(null)

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
}

onMounted(() => {
  const container = bgContainer.value
  if (!container) return

  const icons = ['☕', '☕', '🐞', '🐞', '🚀', '🚀', '💻', '💻', '🧪', '🧪', '🔄', '🔄', '🌐', '🌐']
  const numIcons = 30

  for (let i = 0; i < numIcons; i++) {
    const icon = document.createElement('div')
    icon.className = 'floating-icon'
    icon.textContent = icons[Math.floor(Math.random() * icons.length)]
    const size = Math.floor(Math.random() * 30) + 20
    const left = Math.random() * 100
    const duration = Math.random() * 15 + 8
    const delay = Math.random() * 6
    const opacity = Math.random() * 0.15 + 0.05
    icon.style.fontSize = `${size}px`
    icon.style.left = `${left}%`
    icon.style.animationDuration = `${duration}s`
    icon.style.animationDelay = `${delay}s`
    icon.style.setProperty('--opacity', opacity)
    container.appendChild(icon)
  }
})
</script>

<style scoped>
.json-formatter-page {
  background-color: #121212;
  color: #e0e0e0;
  font-family: sans-serif;
  margin: 0;
  padding: 0;
  min-height: 100vh;
}

.page-title {
  color: #00ff41;
  padding-bottom: 10px;
  display: inline-block;
  margin-top: 0;
  font-size: 1.8rem;
}

.container {
  margin-top: 70px;
  max-width: 1400px;
  margin: 70px auto 0;
  padding: 20px;
  position: relative;
  z-index: 2;
}

.editor-area {
  display: flex;
  gap: 20px;
  margin-top: 30px;
  flex-wrap: wrap;
}

.input-panel {
  flex: 1;
  min-width: 300px;
  background: #1e1e1e;
  border-radius: 8px;
  padding: 15px;
  border: 1px solid #333;
}

.input-panel.collapsed {
  display: none;
}

.editor-area.left-collapsed .output-panel {
  flex: 1;
  min-width: 100%;
}

.output-panel {
  flex: 1;
  min-width: 300px;
  background: #1e1e1e;
  border-radius: 8px;
  padding: 15px;
  border: 1px solid #333;
  overflow: auto;
  max-height: 70vh;
}

textarea {
  width: 100%;
  height: 400px;
  background: #2c2c2c;
  color: #e0e0e0;
  border: 1px solid #444;
  border-radius: 4px;
  padding: 10px;
  font-family: monospace;
  font-size: 14px;
  resize: vertical;
  box-sizing: border-box;
}

textarea:focus {
  outline: none;
  border-color: #00ff41;
}

.button-group {
  margin-top: 15px;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.3s ease;
  display: inline-block;
  text-align: center;
  font-size: 14px;
  width: auto;
}

.btn-primary {
  background-color: #00ff41;
  color: #121212;
}

.btn-primary:hover {
  background-color: #00cc33;
  box-shadow: 0 0 10px rgba(0, 255, 65, 0.5);
}

.btn-danger {
  background-color: #ff4c4c;
  color: white;
}

.btn-danger:hover {
  background-color: #cc0000;
  box-shadow: 0 0 10px rgba(255, 76, 76, 0.5);
}

.error-message {
  color: #ff4c4c;
  margin-top: 10px;
  font-size: 0.9rem;
}

.json-viewer {
  font-family: monospace;
  font-size: 14px;
}

h3 {
  margin-top: 0;
  color: #00ff41;
  font-size: 1.2rem;
}

.toggle-panel-btn {
  background: #333;
  color: #e0e0e0;
  border: 1px solid #555;
  border-radius: 4px;
  padding: 4px 10px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  width: auto;
}

.toggle-panel-btn:hover {
  background: #00ff41;
  color: #121212;
  border-color: #00ff41;
  box-shadow: 0 0 6px #00ff41;
}

.toggle-panel-btn.fixed {
  position: fixed;
  top: 100px;
  right: 20px;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  border-radius: 6px;
}

.floating-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
  overflow: hidden;
  opacity: 0.55;
}

.floating-icon {
  position: absolute;
  user-select: none;
  pointer-events: none;
  animation: float linear infinite;
  filter: blur(0.5px);
}

@keyframes float {
  0% { transform: translateY(100vh) rotate(0deg); opacity: 0; }
  10% { opacity: var(--opacity); }
  90% { opacity: var(--opacity); }
  100% { transform: translateY(-20vh) rotate(360deg); opacity: 0; }
}

@media (max-width: 768px) {
  .editor-area {
    flex-direction: column;
  }
  .toggle-panel-btn.fixed {
    top: 90px;
    right: 10px;
    padding: 6px 12px;
    font-size: 11px;
  }
}
</style>
