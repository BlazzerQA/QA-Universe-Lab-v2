<template>
  <div class="prep-page ui-page" data-testid="collections-page">
    <div class="container ui-page-body">
      <router-link to="/main" class="ui-link" data-testid="collections-back">← На главную</router-link>
      <h2 class="ui-title" data-testid="collections-title">Java Collections: Тренажер</h2>

      <UiCard
        v-for="item in items"
        :key="item.id"
        hover
        class="qa-card"
        :data-testid="'collections-card-' + item.id"
      >
        <div class="question" data-testid="collections-question" @click="toggle(item.id)">
          {{ item.question }}
        </div>
        <div v-show="openId === item.id" class="answer" data-testid="collections-answer">
          {{ item.answer }}
        </div>
        <div class="task-box" :id="item.taskId">{{ item.task }}</div>
        <UiButton variant="primary" data-testid="collections-copy" @click="copyAndGo(item.task)">
          Копировать задачу и открыть редактор
        </UiButton>
      </UiCard>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const openId = ref(null)

const items = [
  {
    id: 1,
    taskId: 'task1',
    question: '1. ArrayList vs LinkedList: в чем разница? ▾',
    answer: 'Ответ: ArrayList — массив (быстрый доступ), LinkedList — список (быстрая вставка в начало).',
    task: '// ЗАДАЧА: Напиши метод, который удаляет дубликаты из ArrayList целых чисел, не используя Set.'
  },
  {
    id: 2,
    taskId: 'task2',
    question: '2. Как работает HashMap? ▾',
    answer:
      'Ответ: Использует hashCode() для поиска корзины и equals() для поиска внутри корзины (связный список/дерево).',
    task: '// ЗАДАЧА: Дан массив строк. Используя HashMap, найди строку, которая встречается чаще всего.'
  },
  {
    id: 3,
    taskId: 'task3',
    question: '3. TreeSet vs HashSet? ▾',
    answer: 'Ответ: HashSet не гарантирует порядок (O(1)), TreeSet хранит элементы в отсортированном виде (O(log n)).',
    task: '// ЗАДАЧА: Создай TreeSet и добавь в него 5 имен. Выведи их в обратном алфавитном порядке.'
  }
]

function toggle(id) {
  openId.value = openId.value === id ? null : id
}

async function copyAndGo(text) {
  const fullCode = 'public class Solution {\n    public static void main(String[] args) {\n        ' + text + '\n    }\n}'
  await navigator.clipboard.writeText(fullCode)
  alert('Код задачи скопирован! Теперь нажми Ctrl+V в редакторе.')
  window.open('https://interview.cups.online/', '_blank')
}
</script>

<style scoped>
.container {
  max-width: 900px;
}

.ui-link {
  margin-bottom: 20px;
  display: inline-block;
}

.qa-card {
  margin-bottom: 20px;
}

.question {
  cursor: pointer;
  font-weight: 700;
  display: block;
  font-size: 1.1em;
  color: var(--heading);
}

.answer {
  margin-top: 15px;
  color: var(--primary);
  border-top: 1px solid var(--border);
  padding-top: 10px;
  font-style: italic;
}

.task-box {
  background: var(--input-bg);
  padding: 10px;
  margin: 15px 0 12px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
  font-family: var(--mono);
  font-size: 0.9em;
  color: var(--warning);
}
</style>
