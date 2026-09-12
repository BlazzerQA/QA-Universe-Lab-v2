import { ref } from 'vue'
import { defineStore } from 'pinia'
import { readPreference, writePreference } from '@/services/preferences'

const STORAGE_KEY = 'qa-lab-theme'
const THEMES = ['dark', 'light']

function normalize(value) {
  return THEMES.includes(value) ? value : 'dark'
}

export const useThemeStore = defineStore('theme', () => {
  const theme = ref(normalize(readPreference(STORAGE_KEY, 'dark')))

  function apply() {
    const value = normalize(theme.value)
    theme.value = value
    document.documentElement.setAttribute('data-theme', value)
    document.documentElement.style.colorScheme = value
  }

  function setTheme(next) {
    theme.value = normalize(next)
    writePreference(STORAGE_KEY, theme.value)
    apply()
  }

  function toggle() {
    setTheme(theme.value === 'dark' ? 'light' : 'dark')
  }

  function init() {
    apply()
  }

  return { theme, setTheme, toggle, init }
})
