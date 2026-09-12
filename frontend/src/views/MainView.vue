<template>
  <div class="main-page ui-page">
    <UiFloatingBg />

    <div class="ui-page-body">
      <div class="button-group">
        <router-link to="/json-formatter" class="ui-btn ui-btn--secondary" data-testid="main-json-formatter">
          JSON Formatter
        </router-link>
        <a href="javascript:void(0)" class="ui-btn ui-btn--danger" data-testid="virus-btn" @click.prevent="handleVirusClick">
          ☠️ НЕ НАЖИМАТЬ!
        </a>
      </div>

      <router-link to="/products-ui" class="topic-card-link" data-testid="main-products">
        <UiCard accent hover>
          <h3>🛒 Магазин товаров</h3>
          <p class="ui-muted">Управление товарами через удобный интерфейс</p>
        </UiCard>
      </router-link>

      <router-link to="/notes" class="topic-card-link" data-testid="main-notes">
        <UiCard accent hover>
          <h3>📝 Knowledge / Notes</h3>
          <p class="ui-muted">Markdown-статьи: Java, API, Git, Selenium, SQL и собеседования</p>
        </UiCard>
      </router-link>

      <router-link to="/collections" class="topic-card-link" data-testid="main-collections">
        <UiCard accent hover>
          <h3>📚 Collections</h3>
          <p class="ui-muted">Q&amp;A и задачи по List, Set, Map</p>
        </UiCard>
      </router-link>

      <router-link to="/mocks" class="topic-card-link" data-testid="main-mocks">
        <UiCard accent hover>
          <h3>🧪 Mocks &amp; Stubs</h3>
          <p class="ui-muted">Mockito: mock, spy, verify, ArgumentCaptor</p>
        </UiCard>
      </router-link>

      <div class="dashboard-placeholder ui-panel">
        <div class="placeholder-icon">🏗️</div>
        <div class="placeholder-text ui-muted">Здесь будет дашборд с метриками. В разработке…</div>
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
import { ref } from 'vue'

const showScaryOverlay = ref(false)

function handleVirusClick() {
  showScaryOverlay.value = true
  setTimeout(() => {
    window.location.href =
      'https://avatars.mds.yandex.net/i?id=7072d2699522c002261da3c214a7b288_l-10638736-images-thumbs&n=13'
  }, 3000)
}
</script>

<style scoped>
.button-group {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.topic-card-link {
  display: block;
  margin-top: 20px;
  text-decoration: none;
  color: inherit;
}

.topic-card-link h3 {
  color: var(--heading);
  margin: 0 0 8px;
}

.topic-card-link p {
  margin: 0;
}

.dashboard-placeholder {
  margin-top: 40px;
  padding: 40px;
  border-style: dashed;
  text-align: center;
}

.placeholder-icon {
  font-size: 3rem;
  margin-bottom: 12px;
  opacity: 0.6;
}

.placeholder-text {
  font-size: 1.1rem;
}

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
  color: var(--danger);
  font-family: var(--mono);
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

@media (max-width: 768px) {
  .button-group { flex-direction: column; }
  .dashboard-placeholder { padding: 24px; }
  .blinking-text { font-size: 1.1rem; }
}
</style>
