<template>
  <div class="prep-page ui-page" data-testid="mocks-page">
    <div class="container ui-page-body">
      <router-link to="/main" class="ui-link" data-testid="mocks-back">← На главную</router-link>
      <h2 class="ui-title" data-testid="mocks-title">Mockito: Изоляция и заглушки</h2>

      <UiCard
        v-for="item in items"
        :key="item.id"
        hover
        class="qa-card"
        :data-testid="'mocks-card-' + item.id"
      >
        <div class="question" data-testid="mocks-question" @click="toggle(item.id)">
          {{ item.question }}
        </div>
        <div v-show="openId === item.id" class="answer" data-testid="mocks-answer">
          {{ item.answer }}
        </div>
        <div class="task-box" :id="item.taskId">{{ item.task }}</div>
        <UiButton variant="primary" data-testid="mocks-copy" @click="copyAndGo(item.task)">Копировать задачу</UiButton>
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
    taskId: 'm1',
    question: '1. В чем разница между Mock и Spy?',
    answer:
      'Ответ: Mock — это полная пустышка, все методы возвращают null/0. Spy — это обертка над реальным объектом, методы работают по-настоящему, если их не "застабить".',
    task: '// ЗАДАЧА: Создай Mock для интерфейса List. Настрой его так, чтобы при вызове list.get(0) возвращалась строка "Mockito".'
  },
  {
    id: 2,
    taskId: 'm2',
    question: '2. Что такое Stubbing (стаббинг)?',
    answer:
      'Ответ: Это процесс жесткого задания поведения метода. Мы говорим: "Когда вызовут метод А с параметром Б, верни результат В". Используются конструкции when(...).thenReturn(...).',
    task: '// ЗАДАЧА: Застабь метод userService.getUserName(1) так, чтобы он выбрасывал RuntimeException.'
  },
  {
    id: 3,
    taskId: 'm3',
    question: '3. Как проверить, что метод был вызван (Verify)?',
    answer:
      'Ответ: С помощью verify(mock).method(). Можно уточнить количество вызовов: times(1), never(), atLeastOnce().',
    task: '// ЗАДАЧА: Проверь, что метод emailService.send() был вызван ровно 2 раза с любым аргументом.'
  },
  {
    id: 4,
    taskId: 'm4',
    question: '4. Что делает ArgumentCaptor?',
    answer:
      'Ответ: Он позволяет "перехватить" аргумент, который был передан в метод мока, чтобы потом проверить его внутренние поля через Assertions.',
    task: '// ЗАДАЧА: Перехвати объект User, переданный в save(user), и проверь, что его поле name равно "Admin".'
  },
  {
    id: 5,
    taskId: 'm5',
    question: '5. Зачем использовать @InjectMocks?',
    answer:
      'Ответ: Эта аннотация автоматически создает тестируемый объект и подставляет (инжектит) в него все созданные моки (@Mock).',
    task: '// ЗАДАЧА: Напиши структуру теста, где PaymentService тестируется с использованием заинжекченного MockPaymentGateway.'
  }
]

function toggle(id) {
  openId.value = openId.value === id ? null : id
}

async function copyAndGo(code) {
  const template =
    'import static org.mockito.Mockito.*;\nimport org.mockito.*;\n\npublic class MockTest {\n    public static void main(String[] args) {\n        ' +
    code +
    '\n    }\n}'
  await navigator.clipboard.writeText(template)
  alert('Задача скопирована! Не забудь выбрать Java на сайте.')
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
  font-size: 1.1em;
  color: var(--heading);
}

.answer {
  margin-top: 15px;
  color: var(--text);
  border-left: 2px solid var(--primary);
  padding-left: 15px;
  font-style: italic;
}

.task-box {
  background: var(--input-bg);
  padding: 10px;
  margin: 15px 0 12px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
  font-family: var(--mono);
  color: var(--warning);
}
</style>
