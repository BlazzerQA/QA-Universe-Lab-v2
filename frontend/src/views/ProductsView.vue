<template>
  <div class="products-page">
    <div class="floating-background" ref="bgContainer"></div>

    <div class="products-container">
      <h2 class="section-title">Магазин товаров</h2>

      <!-- Форма добавления продукта -->
      <div class="form-section">
        <h3>Добавить новый продукт</h3>
        <form @submit.prevent="addProduct">
          <div class="form-group">
            <label for="productName">Название продукта:</label>
            <input
              v-model="form.productName"
              type="text"
              class="form-control"
              :class="{ 'input-error': errors.productName }"
              data-testid="product-name"
            />
          </div>
          <div class="form-group">
            <label for="price">Цена:</label>
            <input
              v-model="form.price"
              type="number"
              class="form-control"
              step="0.01"
              :class="{ 'input-error': errors.price }"
              data-testid="product-price"
            />
          </div>
          <button type="submit" class="btn btn-primary" data-testid="product-add" :disabled="loading">Добавить</button>
        </form>
      </div>

      <!-- Уведомления -->
      <div v-if="notification" class="notification" :class="notification.type">
        {{ notification.message }}
      </div>

      <!-- Список продуктов -->
      <div class="glass-card">
        <h3>Список продуктов</h3>
        <div v-if="products.length === 0" class="empty-state">
          <p style="text-align: center; color: #aaa;">Нет продуктов для отображения</p>
        </div>
        <div v-else>
          <div v-for="product in products" :key="product.productId" class="product-card" data-testid="product-card">
            <div class="product-details">
              <div class="product-name">{{ product.productName }}</div>
              <div class="product-price">Цена: {{ Number(product.price).toFixed(2) }} ₽</div>
              <div class="product-id">ID: {{ product.productId }}</div>
            </div>
            <div class="product-actions">
              <button class="btn btn-danger" data-testid="product-delete" @click="deleteProduct(product.productId)">Удалить</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/client'

const products = ref([])
const loading = ref(false)
const form = ref({ productName: '', price: '' })
const errors = ref({})
const notification = ref(null)
const bgContainer = ref(null)

function showNotification(message, type) {
  notification.value = { message, type }
  setTimeout(() => { notification.value = null }, 5000)
}

async function loadProducts() {
  try {
    const response = await api.get('/api/products')
    products.value = Array.isArray(response.data) ? response.data : Object.values(response.data)
  } catch (error) {
    showNotification('Ошибка загрузки продуктов: ' + (error.message || 'неизвестная ошибка'), 'error')
  }
}

function validateForm() {
  errors.value = {}
  let isValid = true

  const productName = form.value.productName.trim()
  if (!productName) {
    errors.value.productName = true
    showNotification('Название товара не может быть пустым', 'error')
    isValid = false
  } else if (!/[a-zA-Zа-яА-Я0-9]/.test(productName)) {
    errors.value.productName = true
    showNotification('Название товара должно содержать хотя бы одну букву или цифру', 'error')
    isValid = false
  }

  if (form.value.price === '' || form.value.price === null) {
    errors.value.price = true
    showNotification('Введите цену товара', 'error')
    isValid = false
  } else if (isNaN(Number(form.value.price)) || Number(form.value.price) <= 0) {
    errors.value.price = true
    showNotification('Цена должна быть больше 0', 'error')
    isValid = false
  }

  if (!isValid) {
    setTimeout(() => { errors.value = {} }, 3000)
  }

  return isValid
}

async function addProduct() {
  if (!validateForm()) return

  loading.value = true
  try {
    await api.post('/api/products', {
      productName: form.value.productName.trim(),
      price: Number(form.value.price)
    })
    form.value = { productName: '', price: '' }
    await loadProducts()
    showNotification('Продукт успешно добавлен', 'success')
  } catch (error) {
    const msg = error.response?.data?.message || error.message || 'Ошибка добавления'
    showNotification('Ошибка добавления продукта: ' + msg, 'error')
  } finally {
    loading.value = false
  }
}

async function deleteProduct(productId) {
  if (!confirm('Вы уверены, что хотите удалить этот продукт?')) return

  try {
    await api.delete(`/api/products/${productId}`)
    await loadProducts()
    showNotification('Продукт успешно удален', 'success')
  } catch (error) {
    const msg = error.response?.data?.error || error.message || 'Ошибка удаления'
    showNotification('Ошибка удаления продукта: ' + msg, 'error')
  }
}

onMounted(() => {
  loadProducts()

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
.products-page {
  background-color: #121212;
  color: #e0e0e0;
  min-height: 100vh;
}

.products-container {
  max-width: 1200px;
  margin: 50px auto 0;
  padding: 20px;
  position: relative;
  z-index: 2;
  padding-top: 90px;
}

.section-title {
  color: #00ff41;
  text-align: center;
  margin-bottom: 30px;
  font-size: 2rem;
}

.form-section {
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 25px;
  margin-bottom: 30px;
  border: 1px solid rgba(0, 255, 65, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #00ff41;
  font-weight: bold;
}

.form-control {
  width: 100%;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(20, 20, 30, 0.6);
  color: #e0e0e0;
  font-size: 1rem;
  box-sizing: border-box;
}

.form-control.input-error {
  border-color: #ff4c4c;
  box-shadow: 0 0 8px rgba(255, 76, 76, 0.4);
  outline: none;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.3s ease;
  display: inline-block;
  text-align: center;
  width: auto;
  font-size: 1rem;
}

.btn-primary {
  background-color: #00ff41;
  color: #121212;
}

.btn-primary:hover {
  background-color: #00cc33;
  box-shadow: 0 0 10px rgba(0, 255, 65, 0.5);
}

.btn-danger {
  background-color: #ff4c4c;
  color: white;
}

.btn-danger:hover {
  background-color: #cc0000;
  box-shadow: 0 0 10px rgba(255, 76, 76, 0.5);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.glass-card {
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid rgba(0, 255, 65, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.product-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.product-card:last-child {
  border-bottom: none;
}

.product-details {
  flex: 1;
}

.product-name {
  font-weight: bold;
  color: #00ff41;
  margin-bottom: 5px;
}

.product-price {
  color: #ddd;
}

.product-id {
  font-size: 0.8rem;
  color: #aaa;
}

.product-actions {
  display: flex;
  gap: 10px;
}

.notification {
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 20px;
  text-align: center;
  font-weight: bold;
}

.notification.error {
  background-color: rgba(255, 76, 76, 0.2);
  border: 1px solid #ff4c4c;
  color: #ff4c4c;
}

.notification.success {
  background-color: rgba(0, 255, 65, 0.2);
  border: 1px solid #00ff41;
  color: #00ff41;
}

.empty-state {
  padding: 30px 0;
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
  .product-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  .product-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>
