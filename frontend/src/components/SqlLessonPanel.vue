<template>
  <aside class="panel lesson" data-testid="sql-lesson">
    <header class="panel-h">
      <span class="panel-h-left">
        <img class="brand-mark" src="/icons/sql/mark.png" alt="" width="22" height="22" />
        Lesson
      </span>
    </header>
    <div class="panel-body lesson-body">
      <nav class="topic-nav" aria-label="SQL lessons">
        <section v-for="(topic, index) in topics" :key="topic.id" class="topic-section">
          <button
            type="button"
            class="topic-toggle"
            :aria-expanded="isOpen(topic.id)"
            :data-testid="`sql-topic-${topic.id}`"
            @click="$emit('toggle-topic', topic.id)"
          >
            <span class="topic-chevron" aria-hidden="true">{{ isOpen(topic.id) ? '▾' : '▸' }}</span>
            <img class="topic-logo" :src="topic.logo" alt="" width="18" height="18" />
            <span class="topic-title">{{ index + 1 }}. {{ topic.title }}</span>
            <span class="topic-count">{{ topic.items.length }}</span>
          </button>
          <ul v-if="isOpen(topic.id)" class="lesson-list">
            <li v-for="item in topic.items" :key="item.id">
              <button
                type="button"
                class="lesson-link"
                :class="{ active: selectedId === item.id }"
                :data-testid="item.testId"
                :disabled="loading"
                @click="$emit('select-lesson', item.id)"
              >
                {{ item.title }}
              </button>
            </li>
          </ul>
        </section>
      </nav>
      <div class="lesson-meta">
        <SqlFlashCard
          variant="task"
          label="Task"
          glyph="target"
          :items="lesson.tasks"
          test-id="sql-task-card"
          prev-test-id="sql-task-prev"
          next-test-id="sql-task-next"
          prev-label="Предыдущее задание"
          next-label="Следующее задание"
          dots-label="Задания"
        />
        <SqlFlashCard
          variant="hint"
          label="Hint"
          glyph="bulb"
          :items="lesson.hints"
          test-id="sql-hint-card"
          prev-test-id="sql-hint-prev"
          next-test-id="sql-hint-next"
          prev-label="Предыдущая подсказка"
          next-label="Следующая подсказка"
          dots-label="Подсказки"
        />
      </div>
    </div>
  </aside>
</template>

<script setup>
import SqlFlashCard from '@/components/SqlFlashCard.vue'

const props = defineProps({
  topics: { type: Array, required: true },
  selectedId: { type: String, required: true },
  expanded: { type: Object, default: () => ({}) },
  lesson: { type: Object, required: true },
  loading: { type: Boolean, default: false }
})

defineEmits(['toggle-topic', 'select-lesson'])

function isOpen(id) {
  return props.expanded[id] === true
}
</script>

<style scoped>
.lesson-body {
  display: flex;
  flex-direction: column;
  padding: 0;
  overflow: hidden;
}

.topic-nav {
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: 6px 10px 10px;
}

.topic-section {
  display: flex;
  flex-direction: column;
}

.topic-toggle {
  display: flex;
  align-items: center;
  gap: 7px;
  width: 100%;
  margin: 0;
  padding: 7px 4px;
  border: 0;
  border-bottom: 1px solid var(--border);
  border-radius: 0;
  background: transparent;
  color: var(--primary);
  cursor: pointer;
  text-align: left;
}

.topic-toggle:hover {
  background: var(--primary-muted);
}

.topic-chevron {
  width: 0.9rem;
  flex-shrink: 0;
  font-size: 0.75rem;
  color: var(--muted);
}

.topic-logo {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  object-fit: contain;
  background: transparent;
}

.topic-title {
  flex: 1;
  min-width: 0;
  font-size: 0.78rem;
  font-weight: 700;
  letter-spacing: 0.06em;
}

.topic-count {
  font-size: 0.72rem;
  font-weight: 700;
  color: var(--muted);
  font-variant-numeric: tabular-nums;
}

.lesson-list {
  list-style: none;
  margin: 0.1rem 0 0.35rem;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.lesson-list li + li .lesson-link {
  border-top: 1px solid var(--border);
}

.lesson-link {
  display: block;
  width: 100%;
  padding: 8px 10px 8px 12px;
  border: 0;
  border-left: 2px solid transparent;
  border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
  background: none;
  color: var(--text);
  font-size: 0.82rem;
  line-height: 1.35;
  text-align: left;
  cursor: pointer;
}

.lesson-link:hover:not(:disabled),
.lesson-link:focus-visible {
  background: var(--primary-muted);
  color: var(--primary);
  border-left-color: var(--primary);
}

.lesson-link.active {
  background: var(--primary-muted);
  color: var(--primary);
  font-weight: 600;
  border-left-color: var(--primary);
}

.lesson-meta {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 10px;
  border-top: 1px solid var(--border);
}

.brand-mark {
  width: 22px;
  height: 22px;
  border-radius: 6px;
  object-fit: cover;
  border: 1px solid var(--border);
}
</style>
