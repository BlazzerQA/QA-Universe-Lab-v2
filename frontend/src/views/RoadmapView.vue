<template>
  <div class="roadmap-page ui-page">
    <UiFloatingBg />

    <main class="container ui-page-body">
      <section class="hero">
        <h2 class="ui-title">План подготовки к собеседованиям на AQA Java</h2>
        <p class="subtitle ui-muted">Собери фундамент, закрепи практикой и тренируй собес‑навыки как отдельный навык.</p>
      </section>

      <UiCard class="quote-card" aria-label="Цитата">
        <div class="quote-text">
          «Код писать могут ИИ. Но собеседование за тебя не пройдёт ИИ».
        </div>
        <div class="quote-sub ui-muted">
          Твоя цель — уметь объяснить решения, компромиссы и ход мысли.
        </div>
      </UiCard>

      <section class="topic-cards">
        <UiCard accent hover class="topic-card" data-testid="roadmap-json">
          <router-link to="/json-formatter" class="card-link">
            <h3>1. JSON Formatter →</h3>
          </router-link>
          <ul><li>Форматирование и визуализация JSON-данных.</li></ul>
        </UiCard>
        <UiCard accent hover class="topic-card" data-testid="roadmap-products">
          <router-link to="/products-ui" class="card-link">
            <h3>2. Магазин товаров →</h3>
          </router-link>
          <ul><li>CRUD операции через REST API.</li></ul>
        </UiCard>
        <UiCard accent hover class="topic-card" data-testid="roadmap-collections">
          <router-link to="/collections" class="card-link">
            <h3>3. Collections →</h3>
          </router-link>
          <ul><li>List, Set, Map — база для обработки данных в тестах.</li></ul>
        </UiCard>
        <UiCard accent hover class="topic-card" data-testid="roadmap-mocks">
          <router-link to="/mocks" class="card-link">
            <h3>4. Mocks & Stubs →</h3>
          </router-link>
          <ul><li>Mockito — учимся изолировать систему от внешних сервисов.</li></ul>
        </UiCard>
        <UiCard accent hover class="topic-card" data-testid="roadmap-notes">
          <router-link to="/notes" class="card-link">
            <h3>5. Knowledge / Notes →</h3>
          </router-link>
          <ul><li>Статьи из knowledge/: Java, API, Git, JUnit, Selenium, SQL.</li></ul>
        </UiCard>
        <UiCard accent class="topic-card">
          <h3>6. API Testing</h3>
          <ul><li>RestAssured — учимся тестировать бэкенд через код.</li></ul>
        </UiCard>
        <UiCard accent class="topic-card">
          <h3>7. UI Testing</h3>
          <ul><li>Selenide — визуализация действий пользователя.</li></ul>
        </UiCard>
      </section>

      <div class="section-divider">
        <div class="divider-line"></div>
        <div class="divider-text">Основные темы</div>
        <div class="divider-line"></div>
      </div>

      <section class="grid">
        <UiCard accent>
          <h3>1) Java Core</h3>
          <ul>
            <li>OOP, SOLID, исключения, generics</li>
            <li>Collections, Stream API, Optional</li>
            <li>Concurrency: threads, executors, locks</li>
            <li>JVM basics: memory, GC, classloading</li>
          </ul>
        </UiCard>

        <UiCard accent>
          <h3>2) Тестирование (теория)</h3>
          <ul>
            <li>Пирамида тестов, тест‑дизайн</li>
            <li>Flaky tests, стабильность и изоляция</li>
            <li>Что/где мокать: unit vs integration</li>
            <li>Test data, determinism, idempotency</li>
          </ul>
        </UiCard>

        <UiCard accent>
          <h3>3) API Testing</h3>
          <ul>
            <li>HTTP, статусы, headers, cookies</li>
            <li>RestAssured: позитив/негатив/контракт</li>
            <li>Auth: session/JWT, refresh/expiry</li>
            <li>Логи, retries, тесты ошибок</li>
          </ul>
        </UiCard>

        <UiCard accent>
          <h3>4) UI Testing</h3>
          <ul>
            <li>Selenide: Page Object, стабильные локаторы</li>
            <li>Ожидания, синхронизация, сетевые паузы</li>
            <li>Скриншоты, видео, отчёты Allure</li>
            <li>Параллельный запуск, конфиги браузера</li>
          </ul>
        </UiCard>

        <UiCard accent>
          <h3>5) Spring Boot & Data</h3>
          <ul>
            <li>Controllers, validation, exception handling</li>
            <li>JPA basics: entities, relations, transactions</li>
            <li>Testcontainers / embedded DB (где уместно)</li>
            <li>Профили окружений: dev/test</li>
          </ul>
        </UiCard>

        <UiCard accent>
          <h3>6) CI/CD & качество</h3>
          <ul>
            <li>Стабильный pipeline, отчёты, артефакты</li>
            <li>Параллель, шардирование, quarantine</li>
            <li>Code review, читаемость тестов</li>
            <li>Метрики: duration, flaky rate</li>
          </ul>
        </UiCard>
      </section>

      <section class="footer-note">
        <div class="footer-note-inner ui-panel">
          Совет: 2–3 раза в неделю делай «мини‑собес» — объясни вслух один тест/архитектурное решение за 5 минут.
        </div>
      </section>
    </main>
  </div>
</template>

<style scoped>
.hero {
  text-align: center;
  margin: 0 0 24px;
}

.subtitle {
  margin: 0 0 8px;
  line-height: 1.4;
}

.quote-card {
  margin: 0 auto 30px;
  text-align: center;
}

.quote-text {
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--heading);
}

.quote-sub {
  margin-top: 8px;
  font-size: 0.95rem;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.grid h3,
.topic-card h3 {
  margin: 0 0 10px;
  color: var(--heading);
}

.grid ul,
.topic-card ul {
  margin: 0;
  padding-left: 18px;
  color: var(--text);
}

.grid li,
.topic-card li {
  margin: 6px 0;
  color: var(--muted);
}

.footer-note {
  margin: 22px 0 40px;
}

.footer-note-inner {
  padding: 14px 16px;
  color: var(--text);
}

.topic-cards {
  margin: 30px 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.topic-card ul {
  list-style: none;
  padding-left: 0;
}

.card-link {
  text-decoration: none;
  color: inherit;
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
  background: linear-gradient(to right, transparent, var(--primary), transparent);
  opacity: 0.5;
}

.divider-text {
  color: var(--primary);
  font-weight: 600;
  font-size: 1.1rem;
  white-space: nowrap;
  padding: 0 15px;
}

@media (max-width: 900px) {
  .grid { grid-template-columns: 1fr; }
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
