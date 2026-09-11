<template>
  <div class="login-page">
    <canvas id="codeCanvas" ref="codeCanvas"></canvas>

    <div class="login-card" data-testid="login-card">
      <div class="tabs">
        <button
          id="loginTabBtn"
          class="tab-btn"
          :class="{ active: activeTab === 'login' }"
          data-testid="tab-login"
          @click="activeTab = 'login'"
        >
          Вход
        </button>
        <button
          id="registerTabBtn"
          class="tab-btn"
          :class="{ active: activeTab === 'register' }"
          data-testid="tab-register"
          @click="activeTab = 'register'"
        >
          Регистрация
        </button>
      </div>

      <!-- ФОРМА ВХОДА -->
      <div v-show="activeTab === 'login'" class="form-container" data-testid="login-form">
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label>Телефон:</label>
            <input
              v-model="loginForm.phone"
              name="phone"
              type="text"
              placeholder="+79991234567"
              data-testid="login-phone"
            />
            <p v-if="loginErrors.phone" class="error-text" data-testid="login-phone-error">{{ loginErrors.phone }}</p>
          </div>
          <div class="form-group">
            <label>Пароль:</label>
            <input
              v-model="loginForm.password"
              name="password"
              type="password"
              data-testid="login-password"
            />
            <p v-if="loginErrors.global" class="error-text" data-testid="login-error">{{ loginErrors.global }}</p>
          </div>
          <button type="submit" data-testid="login-submit" :disabled="loading">ВОЙТИ</button>
        </form>
      </div>

      <!-- ФОРМА РЕГИСТРАЦИИ -->
      <div v-show="activeTab === 'register'" class="form-container" data-testid="register-form">
        <div class="form-group">
          <label>Телефон:</label>
          <input
            id="regPhone"
            v-model="registerForm.phone"
            type="text"
            placeholder="+79991234567"
            data-testid="register-phone"
          />
          <p v-if="registerErrors.phone" id="regPhoneError" class="error-text" data-testid="register-phone-error">
            {{ registerErrors.phone }}
          </p>
        </div>
        <div class="form-group">
          <label>Пароль (мин. 6 символов):</label>
          <input
            id="regPassword"
            v-model="registerForm.password"
            type="password"
            data-testid="register-password"
          />
          <p v-if="registerErrors.password" id="regPasswordError" class="error-text" data-testid="register-password-error">
            {{ registerErrors.password }}
          </p>
        </div>
        <div class="form-group">
          <label>Повторите пароль:</label>
          <input
            id="regConfirmPassword"
            v-model="registerForm.confirmPassword"
            type="password"
            data-testid="register-confirm"
          />
          <p v-if="registerErrors.confirm" id="regConfirmError" class="error-text" data-testid="register-confirm-error">
            {{ registerErrors.confirm }}
          </p>
        </div>
        <button id="registerBtn" data-testid="register-submit" @click="handleRegister" :disabled="loading">
          ЗАРЕГИСТРИРОВАТЬСЯ
        </button>
        <p v-if="registerMessage" id="registerMessage" class="success-text" data-testid="register-success">
          {{ registerMessage }}
        </p>
        <p v-if="registerErrors.global" id="registerError" class="error-text" data-testid="register-error">
          {{ registerErrors.global }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const activeTab = ref('login')
const loading = ref(false)

const loginForm = ref({ phone: '', password: '' })
const loginErrors = ref({})

const registerForm = ref({ phone: '', password: '', confirmPassword: '' })
const registerErrors = ref({})
const registerMessage = ref('')

const codeCanvas = ref(null)
let animationId = null

async function handleLogin() {
  loginErrors.value = {}

  const phoneRegex = /^\+7\d{10}$/
  if (!phoneRegex.test(loginForm.value.phone)) {
    loginErrors.value.phone = 'Неверный формат номера. Используйте +7XXXXXXXXXX'
    return
  }

  loading.value = true

  try {
    await authStore.login(loginForm.value.phone, loginForm.value.password)
    router.push('/main')
  } catch (error) {
    if (error.response?.data?.error) {
      const apiError = error.response.data.error
      loginErrors.value.global = apiError.endsWith('!') ? apiError : `${apiError}!`
    } else {
      loginErrors.value.global = 'Ошибка входа. Попробуйте позже.'
    }
  } finally {
    loading.value = false
  }
}

function validateRegister() {
  let isValid = true
  registerErrors.value = {}

  const phoneRegex = /^\+7\d{10}$/
  if (!phoneRegex.test(registerForm.value.phone)) {
    registerErrors.value.phone = 'Неверный формат. Используйте +7XXXXXXXXXX'
    isValid = false
  }

  if (registerForm.value.password.length < 6) {
    registerErrors.value.password = 'Пароль должен содержать минимум 6 символов'
    isValid = false
  }

  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    registerErrors.value.confirm = 'Пароли не совпадают'
    isValid = false
  }

  return isValid
}

async function handleRegister() {
  registerMessage.value = ''
  registerErrors.value = {}

  if (!validateRegister()) return

  loading.value = true
  try {
    await authStore.register(registerForm.value.phone, registerForm.value.password)
    registerMessage.value = '✅ Регистрация успешна! Теперь войдите.'
    loginForm.value.phone = registerForm.value.phone
    registerForm.value = { phone: '', password: '', confirmPassword: '' }
    setTimeout(() => {
      activeTab.value = 'login'
    }, 1000)
  } catch (error) {
    if (error.response?.data?.error) {
      registerErrors.value.global = error.response.data.error
    } else {
      registerErrors.value.global = 'Ошибка при регистрации. Попробуйте позже.'
    }
  } finally {
    loading.value = false
  }
}

// Matrix background animation
function initMatrixBg() {
  const canvas = codeCanvas.value
  if (!canvas) return

  const ctx = canvas.getContext('2d')
  let width, height
  const words = [
    'JAVA', 'SPRING', 'JUNIT', 'MOCKITO', 'RESTASSURED',
    'TEST', 'QA', 'CODE', 'AUTOMATION', 'API', 'JSON',
    '{}', '()', '->', ';', 'class', 'public', 'void'
  ]
  const fontSize = 20
  let drops = []

  function resize() {
    width = window.innerWidth
    height = window.innerHeight
    canvas.width = width
    canvas.height = height
    const columns = Math.floor(width / fontSize)
    drops = []
    for (let i = 0; i < columns; i++) {
      drops[i] = Math.random() * -height
    }
  }

  function draw() {
    ctx.fillStyle = 'rgba(18, 18, 18, 0.1)'
    ctx.fillRect(0, 0, width, height)
    ctx.fillStyle = '#33ff55'
    ctx.font = fontSize + 'px monospace'
    for (let i = 0; i < drops.length; i++) {
      const word = words[Math.floor(Math.random() * words.length)]
      const x = i * fontSize
      const y = drops[i] * fontSize
      ctx.fillText(word, x, y)
      if (y > height && Math.random() > 0.975) {
        drops[i] = 0
      }
      drops[i] += 0.6
    }
  }

  function animate() {
    draw()
    animationId = requestAnimationFrame(animate)
  }

  window.addEventListener('resize', resize)
  resize()
  animate()
}

onMounted(() => {
  initMatrixBg()
})

onUnmounted(() => {
  if (animationId) cancelAnimationFrame(animationId)
})
</script>

<style scoped>
.login-page {
  background-color: #121212;
  color: #e0e0e0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
}

#codeCanvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  opacity: 0.35;
  pointer-events: none;
}

.tabs {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
}

.tab-btn {
  background: none;
  border: none;
  color: #aaa;
  font-size: 1rem;
  font-weight: 500;
  padding: 10px 0;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  width: auto;
}

.tab-btn.active {
  color: #00ff41;
}

.tab-btn.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background: #00ff41;
  border-radius: 2px;
  box-shadow: 0 0 6px rgba(0, 255, 65, 0.6);
}

.tab-btn:hover:not(.active)::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background: rgba(0, 255, 65, 0.4);
  border-radius: 2px;
}

.tab-btn:hover:not(.active) {
  color: #ddd;
}

.form-container {
  transition: all 0.3s ease;
}

.login-card {
  position: relative;
  z-index: 1;
  background: rgba(20, 20, 30, 0.35);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  padding: 2rem;
  border-radius: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2), 0 0 0 1px rgba(255, 255, 255, 0.1);
  width: 300px;
  border: none;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: #bbb;
}

input {
  width: 100%;
  padding: 10px;
  background: #2c2c2c;
  border: 1px solid #444;
  border-radius: 4px;
  color: #fff;
  box-sizing: border-box;
}

input:focus {
  border-color: #00ff41;
  outline: none;
}

button[type='submit'],
#registerBtn {
  width: 100%;
  padding: 10px;
  background-color: #00ff41;
  border: none;
  border-radius: 4px;
  color: #000;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
}

button[type='submit']:hover,
#registerBtn:hover {
  background-color: #00cc33;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-text {
  color: #ff4c4c;
  font-size: 0.8rem;
  margin-top: 5px;
}

.success-text {
  color: #00ff41;
  font-size: 0.9rem;
  margin-top: 15px;
  text-align: center;
}
</style>
