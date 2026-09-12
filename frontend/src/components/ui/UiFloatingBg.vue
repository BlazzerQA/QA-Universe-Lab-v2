<template>
  <div class="ui-floating-bg" ref="container" aria-hidden="true"></div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const container = ref(null)

onMounted(() => {
  const root = container.value
  if (!root) return

  const icons = ['☕', '☕', '🐞', '🐞', '🚀', '🚀', '💻', '💻', '🧪', '🧪', '🔄', '🔄', '🌐', '🌐']

  for (let i = 0; i < 30; i++) {
    const icon = document.createElement('div')
    icon.className = 'floating-icon'
    icon.textContent = icons[Math.floor(Math.random() * icons.length)]
    icon.style.fontSize = `${Math.floor(Math.random() * 30) + 20}px`
    icon.style.left = `${Math.random() * 100}%`
    icon.style.animationDuration = `${Math.random() * 15 + 8}s`
    icon.style.animationDelay = `${Math.random() * 6}s`
    icon.style.setProperty('--opacity', String(Math.random() * 0.15 + 0.05))
    root.appendChild(icon)
  }
})
</script>
