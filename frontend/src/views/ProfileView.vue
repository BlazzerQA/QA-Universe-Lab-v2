<template>
  <div class="profile-page">
    <div class="floating-background" ref="bgContainer"></div>

    <div class="container">
      <div class="dashboard">
        <div class="profile-header">
          <h1>👤 Мой профиль</h1>
        </div>

        <div class="profile-section">
          <h2>Личные данные</h2>
          <div class="profile-grid">
            <div class="profile-item">
              <label>Телефон</label>
              <input v-model="profile.phone" type="text" readonly data-testid="profile-phone" />
            </div>
            <div class="profile-item">
              <label>Имя</label>
              <input
                v-model="fullName"
                type="text"
                placeholder="Введите ваше имя"
                maxlength="50"
                data-testid="profile-fullname"
              />
            </div>
          </div>
          <button class="save-btn" data-testid="profile-save" @click="saveProfile" :disabled="saving">
            💾 Сохранить изменения
          </button>
        </div>

        <div class="profile-section">
          <h2>Статистика</h2>
          <div class="stats-placeholder">
            <p>Тут может быть блок статистики</p>
          </div>
        </div>

        <div class="profile-section">
          <h2>Действия</h2>
          <div class="actions-grid">
            <router-link to="/roadmap" class="action-btn" data-testid="profile-roadmap">📋 Roadmap</router-link>
            <router-link to="/collections" class="action-btn" data-testid="profile-collections">📚 Collections</router-link>
            <router-link to="/mocks" class="action-btn" data-testid="profile-mocks">🧪 Mocks</router-link>
            <router-link to="/notes" class="action-btn" data-testid="profile-notes">📝 Notes</router-link>
            <router-link to="/json-formatter" class="action-btn" data-testid="profile-json">📄 JSON Formatter</router-link>
            <router-link to="/products-ui" class="action-btn" data-testid="profile-products">🛒 Магазин товаров</router-link>
          </div>
        </div>

        <p v-if="message" class="message" :style="{ color: messageColor }">{{ message }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/client'

const profile = ref({ phone: '', fullName: '' })
const fullName = ref('')
const message = ref('')
const messageColor = ref('#00ff41')
const saving = ref(false)
const bgContainer = ref(null)

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
    messageColor.value = '#00ff41'
    profile.value.fullName = response.data.fullName
    setTimeout(() => { message.value = '' }, 3000)
  } catch (error) {
    message.value = 'Ошибка: ' + (error.response?.data?.error || 'неизвестная ошибка')
    messageColor.value = '#ff4c4c'
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadProfile()

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
.profile-page {
  background-color: #121212;
  color: #e0e0e0;
  font-family: sans-serif;
  margin: 0;
  padding: 0;
  min-height: 100vh;
}

.container {
  margin-top: 70px;
  padding: 2rem;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  position: relative;
  z-index: 2;
  padding-top: 100px;
}

.dashboard {
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(12px);
  border-radius: 24px;
  padding: 2rem;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.profile-header {
  text-align: center;
  margin-bottom: 2rem;
}

.profile-header h1 {
  color: #00ff41;
  margin: 0;
  font-size: 2rem;
}

.profile-section {
  margin-bottom: 2.5rem;
}

.profile-section h2 {
  color: #00ff41;
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding-bottom: 0.5rem;
}

.profile-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.profile-item {
  display: flex;
  flex-direction: column;
}

.profile-item label {
  display: block;
  margin-bottom: 0.5rem;
  color: #bbb;
  font-weight: 500;
}

.profile-item input {
  width: 100%;
  padding: 12px;
  background: #2c2c2c;
  border: 1px solid #444;
  border-radius: 8px;
  color: #fff;
  box-sizing: border-box;
  font-size: 1rem;
}

.profile-item input:focus {
  outline: none;
  border-color: #00ff41;
}

.profile-item input[readonly] {
  background: #1a1a1a;
  cursor: not-allowed;
}

.save-btn {
  padding: 12px 30px;
  background: linear-gradient(135deg, #00ff41, #00cc33);
  border: none;
  border-radius: 8px;
  color: #121212;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  display: inline-block;
  margin-top: 1rem;
  box-shadow: 0 4px 15px rgba(0, 255, 65, 0.2);
  border: 1px solid rgba(0, 255, 65, 0.3);
}

.save-btn:hover {
  background: linear-gradient(135deg, #00cc33, #009900);
  box-shadow: 0 6px 20px rgba(0, 255, 65, 0.4);
  transform: translateY(-2px);
}

.save-btn:active {
  transform: translateY(0);
}

.save-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.stats-placeholder {
  background: rgba(40, 40, 50, 0.6);
  border-radius: 16px;
  padding: 2rem;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.05);
  color: #bbb;
}

.stats-placeholder p {
  margin: 0;
  font-size: 1.1rem;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.action-btn {
  display: block;
  padding: 1rem;
  background: rgba(40, 40, 50, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: #00ff41;
  text-align: center;
  text-decoration: none;
  transition: all 0.3s;
  font-weight: 500;
}

.action-btn:hover {
  background: rgba(0, 255, 65, 0.1);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 255, 65, 0.2);
}

.message {
  text-align: center;
  margin-top: 15px;
  font-size: 1rem;
  padding: 12px;
  border-radius: 8px;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
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
  .container {
    padding: 1rem;
    padding-top: 100px;
  }
  .dashboard {
    padding: 1.5rem;
  }
  .profile-grid,
  .actions-grid {
    grid-template-columns: 1fr;
  }
  .profile-header h1 {
    font-size: 1.5rem;
  }
}
</style>
