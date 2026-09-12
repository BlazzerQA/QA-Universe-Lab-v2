<template>
  <div class="login-page ui-page">
    <canvas id="codeCanvas" ref="codeCanvas"></canvas>

    <UiCard class="login-card" data-testid="login-card">
      <UiTabs v-model="activeTab" :tabs="tabs" />

      <div v-show="activeTab === 'login'" data-testid="login-form">
        <form @submit.prevent="handleLogin">
          <UiInput
            v-model="loginForm.phone"
            label="Телефон:"
            name="phone"
            placeholder="+79991234567"
            data-testid="login-phone"
            :error="loginErrors.phone"
            error-testid="login-phone-error"
          />
          <UiInput
            v-model="loginForm.password"
            label="Пароль:"
            name="password"
            type="password"
            data-testid="login-password"
          />
          <p v-if="loginErrors.global" class="ui-hint ui-hint--error" data-testid="login-error">
            {{ loginErrors.global }}
          </p>
          <UiButton type="submit" variant="primary" block data-testid="login-submit" :disabled="loading">
            ВОЙТИ
          </UiButton>
        </form>
      </div>

      <div v-show="activeTab === 'register'" data-testid="register-form">
        <UiInput
          v-model="registerForm.phone"
          input-id="regPhone"
          label="Телефон:"
          placeholder="+79991234567"
          data-testid="register-phone"
          :error="registerErrors.phone"
          error-id="regPhoneError"
          error-testid="register-phone-error"
        />
        <UiInput
          v-model="registerForm.password"
          input-id="regPassword"
          label="Пароль (мин. 6 символов):"
          type="password"
          data-testid="register-password"
          :error="registerErrors.password"
          error-id="regPasswordError"
          error-testid="register-password-error"
        />
        <UiInput
          v-model="registerForm.confirmPassword"
          input-id="regConfirmPassword"
          label="Повторите пароль:"
          type="password"
          data-testid="register-confirm"
          :error="registerErrors.confirm"
          error-id="regConfirmError"
          error-testid="register-confirm-error"
        />
        <UiButton
          id="registerBtn"
          variant="primary"
          block
          data-testid="register-submit"
          :disabled="loading"
          @click="handleRegister"
        >
          ЗАРЕГИСТРИРОВАТЬСЯ
        </UiButton>
        <p v-if="registerMessage" id="registerMessage" class="ui-hint ui-hint--success" data-testid="register-success">
          {{ registerMessage }}
        </p>
        <p v-if="registerErrors.global" id="registerError" class="ui-hint ui-hint--error" data-testid="register-error">
          {{ registerErrors.global }}
        </p>
      </div>
    </UiCard>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const tabs = [
  { id: 'login', label: 'Вход', testid: 'tab-login', buttonId: 'loginTabBtn' },
  { id: 'register', label: 'Регистрация', testid: 'tab-register', buttonId: 'registerTabBtn' }
]

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

  function token(name, fallback) {
    return getComputedStyle(document.documentElement).getPropertyValue(name).trim() || fallback
  }

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
    ctx.fillStyle = 'rgba(11, 18, 32, 0.14)'
    ctx.fillRect(0, 0, width, height)
    ctx.fillStyle = token('--primary', '#3b82f6')
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
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
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

.login-card {
  position: relative;
  z-index: 1;
  width: 340px;
  padding: 2rem;
  backdrop-filter: blur(16px);
}
</style>
