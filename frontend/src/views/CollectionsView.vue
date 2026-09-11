<template>
  <div class="prep-page" data-testid="collections-page">
    <div class="container">
      <router-link to="/main" class="back-link" data-testid="collections-back">← На главную</router-link>
      <h2 data-testid="collections-title">Java Collections: Тренажер</h2>

      <div
        v-for="item in items"
        :key="item.id"
        class="card"
        :data-testid="'collections-card-' + item.id"
      >
        <div class="question" data-testid="collections-question" @click="toggle(item.id)">
          {{ item.question }}
        </div>
        <div v-show="openId === item.id" class="answer" data-testid="collections-answer">
          {{ item.answer }}
        </div>
        <div class="task-box" :id="item.taskId">{{ item.task }}</div>
        <button class="copy-btn" data-testid="collections-copy" @click="copyAndGo(item.task)">
          Копировать задачу и открыть редактор
        </button>
      </div>
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
.prep-page {
  background-color: #121212;
  color: #e0e0e0;
  font-family: sans-serif;
  padding: 30px;
  min-height: 100vh;
  box-sizing: border-box;
}

.container {
  max-width: 900px;
  margin: 90px auto 0;
}

.back-link {
  color: #00ff41;
  text-decoration: none;
  margin-bottom: 20px;
  display: inline-block;
}

.card {
  background: #1e1e1e;
  margin-bottom: 20px;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #333;
  position: relative;
}

.card:hover {
  border-color: #00ff41;
}

.question {
  cursor: pointer;
  font-weight: bold;
  display: block;
  font-size: 1.1em;
}

.answer {
  margin-top: 15px;
  color: #00ff41;
  border-top: 1px solid #333;
  padding-top: 10px;
  font-style: italic;
}

.task-box {
  background: #2a2a2a;
  padding: 10px;
  margin-top: 15px;
  border-radius: 4px;
  font-family: monospace;
  font-size: 0.9em;
  color: #ffca28;
}

.copy-btn {
  margin-top: 10px;
  background: #00ff41;
  color: #000;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.copy-btn:active {
  transform: scale(0.95);
}

h2 {
  color: #00ff41;
}
</style>
