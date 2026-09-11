<template>
  <div class="roadmap-page">
    <div class="floating-background" ref="bgContainer"></div>

    <main class="container">
      <section class="hero">
        <h2>План подготовки к собеседованиям на AQA Java</h2>
        <p class="subtitle">Собери фундамент, закрепи практикой и тренируй собес‑навыки как отдельный навык.</p>
      </section>

      <section class="quote-card" aria-label="Цитата">
        <div class="quote-text">
          «Код писать могут ИИ. Но собеседование за тебя не пройдёт ИИ».
        </div>
        <div class="quote-sub">
          Твоя цель — уметь объяснить решения, компромиссы и ход мысли.
        </div>
      </section>

      <section class="topic-cards">
        <article class="topic-card clickable" data-testid="roadmap-json">
          <router-link to="/json-formatter" class="card-link">
            <h3>1. JSON Formatter →</h3>
          </router-link>
          <ul><li>Форматирование и визуализация JSON-данных.</li></ul>
        </article>
        <article class="topic-card clickable" data-testid="roadmap-products">
          <router-link to="/products-ui" class="card-link">
            <h3>2. Магазин товаров →</h3>
          </router-link>
          <ul><li>CRUD операции через REST API.</li></ul>
        </article>
        <article class="topic-card clickable" data-testid="roadmap-collections">
          <router-link to="/collections" class="card-link">
            <h3>3. Collections →</h3>
          </router-link>
          <ul><li>List, Set, Map — база для обработки данных в тестах.</li></ul>
        </article>
        <article class="topic-card clickable" data-testid="roadmap-mocks">
          <router-link to="/mocks" class="card-link">
            <h3>4. Mocks & Stubs →</h3>
          </router-link>
          <ul><li>Mockito — учимся изолировать систему от внешних сервисов.</li></ul>
        </article>
        <article class="topic-card clickable" data-testid="roadmap-notes">
          <router-link to="/notes" class="card-link">
            <h3>5. Knowledge / Notes →</h3>
          </router-link>
          <ul><li>Статьи из knowledge/: Java, API, Git, JUnit, Selenium, SQL.</li></ul>
        </article>
        <article class="topic-card">
          <h3>6. API Testing</h3>
          <ul><li>RestAssured — учимся тестировать бэкенд через код.</li></ul>
        </article>
        <article class="topic-card">
          <h3>7. UI Testing</h3>
          <ul><li>Selenide — визуализация действий пользователя.</li></ul>
        </article>
      </section>

      <div class="section-divider">
        <div class="divider-line"></div>
        <div class="divider-text">Основные темы</div>
        <div class="divider-line"></div>
      </div>

      <section class="grid">
        <article class="card">
          <h3>1) Java Core</h3>
          <ul>
            <li>OOP, SOLID, исключения, generics</li>
            <li>Collections, Stream API, Optional</li>
            <li>Concurrency: threads, executors, locks</li>
            <li>JVM basics: memory, GC, classloading</li>
          </ul>
        </article>

        <article class="card">
          <h3>2) Тестирование (теория)</h3>
          <ul>
            <li>Пирамида тестов, тест‑дизайн</li>
            <li>Flaky tests, стабильность и изоляция</li>
            <li>Что/где мокать: unit vs integration</li>
            <li>Test data, determinism, idempotency</li>
          </ul>
        </article>

        <article class="card">
          <h3>3) API Testing</h3>
          <ul>
            <li>HTTP, статусы, headers, cookies</li>
            <li>RestAssured: позитив/негатив/контракт</li>
            <li>Auth: session/JWT, refresh/expiry</li>
            <li>Логи, retries, тесты ошибок</li>
          </ul>
        </article>

        <article class="card">
          <h3>4) UI Testing</h3>
          <ul>
            <li>Selenide: Page Object, стабильные локаторы</li>
            <li>Ожидания, синхронизация, сетевые паузы</li>
            <li>Скриншоты, видео, отчёты Allure</li>
            <li>Параллельный запуск, конфиги браузера</li>
          </ul>
        </article>

        <article class="card">
          <h3>5) Spring Boot & Data</h3>
          <ul>
            <li>Controllers, validation, exception handling</li>
            <li>JPA basics: entities, relations, transactions</li>
            <li>Testcontainers / embedded DB (где уместно)</li>
            <li>Профили окружений: dev/test</li>
          </ul>
        </article>

        <article class="card">
          <h3>6) CI/CD & качество</h3>
          <ul>
            <li>Стабильный pipeline, отчёты, артефакты</li>
            <li>Параллель, шардирование, quarantine</li>
            <li>Code review, читаемость тестов</li>
            <li>Метрики: duration, flaky rate</li>
          </ul>
        </article>
      </section>

      <section class="footer-note">
        <div class="footer-note-inner">
          Совет: 2–3 раза в неделю делай «мини‑собес» — объясни вслух один тест/архитектурное решение за 5 минут.
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const bgContainer = ref(null)

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
.roadmap-page {
  background-color: #121212;
  color: #e0e0e0;
  font-family: sans-serif;
  padding: 50px;
  min-height: 100vh;
}

.container {
  max-width: 980px;
  margin: 110px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 2;
}

.hero {
  text-align: center;
  margin: 40px 0;
}

.hero h2 {
  margin: 0 0 10px;
  color: #eaeaea;
  font-size: 1.8rem;
}

.subtitle {
  margin: 0 0 24px;
  color: #a8a8a8;
  line-height: 1.4;
}

.quote-card {
  margin: 20px auto 30px;
  padding: 22px 24px;
  border-radius: 16px;
  border: 1px solid rgba(0, 255, 65, 0.25);
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(10px);
  text-align: center;
}

.quote-text {
  font-size: 1.15rem;
  font-weight: 700;
  color: #00ff41;
  letter-spacing: 0.2px;
}

.quote-sub {
  margin-top: 8px;
  color: #bcbcbc;
  font-size: 0.95rem;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.card {
  background: rgba(30, 30, 40, 0.45);
  border-radius: 14px;
  border-left: 5px solid #00ff41;
  border-right: 1px solid rgba(255, 255, 255, 0.08);
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  padding: 18px 18px 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.card h3 {
  margin: 0 0 10px;
  color: #00ff41;
}

.card ul {
  margin: 0;
  padding-left: 18px;
  color: #d0d0d0;
}

.card li {
  margin: 6px 0;
  color: #bbb;
}

.footer-note {
  margin: 22px 0 40px;
}

.footer-note-inner {
  padding: 14px 16px;
  border-radius: 12px;
  background: rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.08);
  color: #cfcfcf;
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
</style>

<style>
/* Глобальные стили для динамически создаваемых floating-иконок (scoped не применяется) */
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
</style>

<style scoped>
.topic-cards {
  margin: 30px 0;
}

.topic-card {
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  margin: 15px 0;
  padding: 20px;
  border-radius: 12px;
  border-left: 5px solid #00ff41;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.2s, box-shadow 0.2s;
}

.topic-card.clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
  background: rgba(30, 30, 40, 0.6);
}

.topic-card h3 {
  color: #00ff41;
  margin-top: 0;
  margin-bottom: 10px;
}

.topic-card ul {
  list-style: none;
  padding-left: 0;
  margin: 0;
}

.topic-card li {
  margin-bottom: 10px;
  color: #bbb;
}

.card-link {
  text-decoration: none;
}

.section-divider {
  display: flex;
  align-items: center;
  margin: 40px 0;
  gap: 20px;
}

.divider-line {
  flex: 1;
  height: 1px;
  background: linear-gradient(to right, transparent, rgba(0, 255, 65, 0.5), transparent);
}

.divider-text {
  color: #00ff41;
  font-weight: 600;
  font-size: 1.1rem;
  white-space: nowrap;
  padding: 0 15px;
}

@media (max-width: 900px) {
  .roadmap-page { padding: 22px; }
  .grid { grid-template-columns: 1fr; }
  .topic-cards { grid-template-columns: 1fr; }
  .section-divider {
    margin: 30px 0;
    gap: 15px;
  }
  .divider-text {
    font-size: 1rem;
    padding: 0 10px;
  }
}
</style>
