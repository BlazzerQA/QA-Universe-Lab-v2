<template>
  <div id="app-root">
    <AppHeader v-if="!isLoginPage" />
    <router-view />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from './components/AppHeader.vue'

const route = useRoute()
const isLoginPage = computed(() => route.path === '/login')
</script>

<style>
#app-root {
  min-height: 100vh;
  background-color: #121212;
}
</style>
