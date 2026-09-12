import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './assets/styles/tokens.css'
import './assets/styles/base.css'
import UiButton from './components/ui/UiButton.vue'
import UiInput from './components/ui/UiInput.vue'
import UiCard from './components/ui/UiCard.vue'
import UiTabs from './components/ui/UiTabs.vue'
import UiFloatingBg from './components/ui/UiFloatingBg.vue'
import { useThemeStore } from './stores/theme'
import { useLocaleStore } from './stores/locale'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

useThemeStore().init()
useLocaleStore().init()

app.component('UiButton', UiButton)
app.component('UiInput', UiInput)
app.component('UiCard', UiCard)
app.component('UiTabs', UiTabs)
app.component('UiFloatingBg', UiFloatingBg)

app.mount('#app')
