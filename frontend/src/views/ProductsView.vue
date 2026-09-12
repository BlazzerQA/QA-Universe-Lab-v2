<template>
  <div class="products-page ui-page">
    <UiFloatingBg />

    <div class="products-container ui-page-body">
      <h2 class="ui-title">Магазин товаров</h2>

      <UiCard class="form-section">
        <h3>Добавить новый продукт</h3>
        <form @submit.prevent="addProduct">
          <UiInput
            v-model="form.productName"
            input-id="productName"
            label="Название продукта:"
            :invalid="!!errors.productName"
            data-testid="product-name"
          />
          <UiInput
            v-model="form.price"
            input-id="price"
            label="Цена:"
            type="number"
            step="0.01"
            :invalid="!!errors.price"
            data-testid="product-price"
          />
          <UiButton type="submit" variant="primary" data-testid="product-add" :disabled="loading">Добавить</UiButton>
        </form>
      </UiCard>

      <div v-if="notification" class="ui-banner" :class="notification.type === 'error' ? 'ui-banner--error' : 'ui-banner--success'">
        {{ notification.message }}
      </div>

      <UiCard>
        <h3>Список продуктов</h3>
        <div v-if="products.length === 0" class="empty-state">
          <p class="ui-muted" style="text-align: center;">Нет продуктов для отображения</p>
        </div>
        <div v-else>
          <div v-for="product in products" :key="product.productId" class="product-card" data-testid="product-card">
            <div class="product-details">
              <div class="product-name">{{ product.productName }}</div>
              <div class="product-price">Цена: {{ Number(product.price).toFixed(2) }} ₽</div>
              <div class="product-id ui-muted">ID: {{ product.productId }}</div>
            </div>
            <div class="product-actions">
              <UiButton variant="danger" data-testid="product-delete" @click="deleteProduct(product.productId)">Удалить</UiButton>
            </div>
          </div>
        </div>
      </UiCard>
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
})
</script>

<style scoped>
.products-container {
  max-width: 1200px;
}

.form-section {
  margin-bottom: 24px;
}

.form-section h3,
.ui-card h3 {
  margin: 0 0 16px;
  color: var(--heading);
}

.product-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid var(--border);
}

.product-card:last-child {
  border-bottom: none;
}

.product-name {
  font-weight: 700;
  color: var(--heading);
  margin-bottom: 5px;
}

.product-price {
  color: var(--text);
}

.product-id {
  font-size: 0.8rem;
}

.product-actions {
  display: flex;
  gap: 10px;
}

.empty-state {
  padding: 30px 0;
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
