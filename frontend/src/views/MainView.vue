<template>
  <div class="main-page">
    <div class="floating-background" ref="bgContainer"></div>

    <div class="container">
      <div class="button-group">
        <router-link to="/json-formatter" class="json-btn" data-testid="main-json-formatter">JSON Formatter</router-link>
        <a href="javascript:void(0)" class="virus-btn" data-testid="virus-btn" @click.prevent="handleVirusClick">
          ☠️ НЕ НАЖИМАТЬ!
        </a>
      </div>

      <router-link to="/products-ui" class="topic-card-link" data-testid="main-products">
        <div class="topic-card">
          <h3>🛒 Магазин товаров</h3>
          <p>Управление товарами через удобный интерфейс</p>
        </div>
      </router-link>

      <router-link to="/notes" class="topic-card-link" data-testid="main-notes">
        <div class="topic-card">
          <h3>📝 Knowledge / Notes</h3>
          <p>Markdown-статьи: Java, API, Git, Selenium, SQL и собеседования</p>
        </div>
      </router-link>

      <router-link to="/collections" class="topic-card-link" data-testid="main-collections">
        <div class="topic-card">
          <h3>📚 Collections</h3>
          <p>Q&amp;A и задачи по List, Set, Map</p>
        </div>
      </router-link>

      <router-link to="/mocks" class="topic-card-link" data-testid="main-mocks">
        <div class="topic-card">
          <h3>🧪 Mocks &amp; Stubs</h3>
          <p>Mockito: mock, spy, verify, ArgumentCaptor</p>
        </div>
      </router-link>

      <div class="dashboard-placeholder">
        <div class="placeholder-icon">🏗️</div>
        <div class="placeholder-text">Здесь будет дашборд с метриками. В разработке…</div>
      </div>
    </div>

    <Teleport to="body">
      <div id="scaryOverlay" class="scary-overlay" v-show="showScaryOverlay">
        <div class="scary-message">
          <div class="blinking-text">
            ⚠️ ВНИМАНИЕ! ЗАПУЩЕНА СИСТЕМА УДАЛЕНИЯ ОПЕРАЦИОННОЙ СИСТЕМЫ! ⚠️
          </div>
          <div class="exclamation-marks">
            !!! !!! !!!
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const bgContainer = ref(null)
const showScaryOverlay = ref(false)

function handleVirusClick() {
  showScaryOverlay.value = true
  setTimeout(() => {
    window.location.href =
      'https://avatars.mds.yandex.net/i?id=7072d2699522c002261da3c214a7b288_l-10638736-images-thumbs&n=13'
  }, 3000)
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
    icon.style.opacity = opacity

    container.appendChild(icon)
  }
})
</script>

<style scoped>
.main-page {
  background-color: #121212;
  color: #e0e0e0;
  font-family: sans-serif;
  padding: 50px;
  min-height: 100vh;
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

.container {
  max-width: 980px;
  margin: 110px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 2;
}

.button-group {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.json-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 14px 28px;
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: #00ff41;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.2s ease;
}

.json-btn:hover {
  background: rgba(30, 30, 40, 0.7);
  border-color: #00ff41;
  transform: translateY(-1px);
}

.virus-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 14px 28px;
  background: rgba(100, 20, 20, 0.45);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 80, 80, 0.3);
  border-radius: 12px;
  color: #ff5555;
  font-weight: 600;
  text-decoration: none;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.virus-btn:hover {
  background: rgba(150, 30, 30, 0.6);
  border-color: #ff3333;
  box-shadow: 0 0 15px rgba(255, 50, 50, 0.3);
  transform: translateY(-1px);
}

.topic-card-link {
  display: block;
  margin-top: 20px;
  text-decoration: none;
}

.topic-card {
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  padding: 20px;
  border-radius: 12px;
  border-left: 5px solid #00ff41;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.topic-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
  background: rgba(30, 30, 40, 0.6);
}

.topic-card h3 {
  color: #00ff41;
  margin: 0 0 8px;
}

.topic-card p {
  margin: 0;
  color: #bbb;
}

.dashboard-placeholder {
  margin-top: 40px;
  padding: 40px;
  border-radius: 16px;
  background: rgba(30, 30, 40, 0.25);
  border: 2px dashed rgba(0, 255, 65, 0.2);
  text-align: center;
}

.placeholder-icon {
  font-size: 3rem;
  margin-bottom: 12px;
  opacity: 0.6;
}

.placeholder-text {
  color: #888;
  font-size: 1.1rem;
}

/* Scary overlay */
.scary-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.95);
  z-index: 10000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.scary-message {
  text-align: center;
  color: #ff0000;
  font-family: monospace;
}

.blinking-text {
  font-size: 1.5rem;
  font-weight: bold;
  animation: blink 0.5s infinite;
}

.exclamation-marks {
  font-size: 3rem;
  margin-top: 20px;
  animation: shake 0.3s infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

@keyframes shake {
  0% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
  100% { transform: translateX(0); }
}

/* Floating icons animation */
.floating-icon {
  position: absolute;
  user-select: none;
  pointer-events: none;
  animation: float linear infinite;
  filter: blur(0.5px);
}

@keyframes float {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% { opacity: var(--opacity); }
  90% { opacity: var(--opacity); }
  100% {
    transform: translateY(-20vh) rotate(360deg);
    opacity: 0;
  }
}

@media (max-width: 768px) {
  .main-page { padding: 22px; }
  .button-group { flex-direction: column; }
  .dashboard-placeholder { padding: 24px; }
  .blinking-text { font-size: 1.1rem; }
}
</style>
