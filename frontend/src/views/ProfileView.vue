<template>
  <div class="profile-page ui-page">
    <UiFloatingBg />

    <div class="container ui-page-body">
      <UiCard class="dashboard">
        <div class="profile-header">
          <h1 class="ui-title">👤 Мой профиль</h1>
        </div>

        <div class="profile-section">
          <h2>Личные данные</h2>
          <div class="profile-grid">
            <UiInput v-model="profile.phone" label="Телефон" readonly data-testid="profile-phone" />
            <UiInput
              v-model="fullName"
              label="Имя"
              placeholder="Введите ваше имя"
              maxlength="50"
              data-testid="profile-fullname"
            />
          </div>
          <UiButton variant="primary" data-testid="profile-save" :disabled="saving" @click="saveProfile">
            💾 Сохранить изменения
          </UiButton>
        </div>

        <div class="profile-section">
          <h2>Статистика</h2>
          <div class="stats-placeholder ui-panel">
            <p class="ui-muted">Тут может быть блок статистики</p>
          </div>
        </div>

        <div class="profile-section">
          <h2>Действия</h2>
          <div class="actions-grid">
            <router-link to="/roadmap" class="ui-btn ui-btn--secondary" data-testid="profile-roadmap">📋 Roadmap</router-link>
            <router-link to="/collections" class="ui-btn ui-btn--secondary" data-testid="profile-collections">📚 Collections</router-link>
            <router-link to="/mocks" class="ui-btn ui-btn--secondary" data-testid="profile-mocks">🧪 Mocks</router-link>
            <router-link to="/notes" class="ui-btn ui-btn--secondary" data-testid="profile-notes">📝 Notes</router-link>
            <router-link to="/json-formatter" class="ui-btn ui-btn--secondary" data-testid="profile-json">📄 JSON Formatter</router-link>
            <router-link to="/products-ui" class="ui-btn ui-btn--secondary" data-testid="profile-products">🛒 Магазин товаров</router-link>
          </div>
        </div>

        <p v-if="message" class="ui-hint" :class="messageOk ? 'ui-hint--success' : 'ui-hint--error'">{{ message }}</p>
      </UiCard>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/client'

const profile = ref({ phone: '', fullName: '' })
const fullName = ref('')
const message = ref('')
const messageOk = ref(true)
const saving = ref(false)

async function loadProfile() {
  try {
    const response = await api.get('/api/auth/profile')
    profile.value = response.data
    fullName.value = response.data.fullName || ''
  } catch (error) {
    if (error.response?.status === 401) {
      window.location.href = '/login'
    }
  }
}

async function saveProfile() {
  saving.value = true
  message.value = ''

  let name = fullName.value
  if (name) name = name.trim()

  try {
    const response = await api.put('/api/auth/profile', { fullName: name })
    message.value = response.data.message
    messageOk.value = true
    profile.value.fullName = response.data.fullName
    setTimeout(() => { message.value = '' }, 3000)
  } catch (error) {
    message.value = 'Ошибка: ' + (error.response?.data?.error || 'неизвестная ошибка')
    messageOk.value = false
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.container {
  max-width: 1200px;
}

.dashboard {
  padding: 2rem;
}

.profile-header {
  text-align: center;
  margin-bottom: 2rem;
}

.profile-section {
  margin-bottom: 2.5rem;
}

.profile-section h2 {
  color: var(--heading);
  margin: 0 0 1.5rem;
  font-size: 1.5rem;
  border-bottom: 1px solid var(--border);
  padding-bottom: 0.5rem;
}

.profile-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 0.5rem;
}

.stats-placeholder {
  padding: 2rem;
  text-align: center;
}

.stats-placeholder p {
  margin: 0;
  font-size: 1.1rem;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
}

@media (max-width: 768px) {
  .dashboard {
    padding: 1.5rem;
  }
  .profile-grid,
  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>
