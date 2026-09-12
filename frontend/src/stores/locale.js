import { ref } from 'vue'
import { defineStore } from 'pinia'
import { DEFAULT_LOCALE, LOCALES } from '@/i18n/messages'
import { readPreference, writePreference } from '@/services/preferences'

const STORAGE_KEY = 'qa-lab-locale'

function normalize(value) {
  return LOCALES.includes(value) ? value : DEFAULT_LOCALE
}

export const useLocaleStore = defineStore('locale', () => {
  const locale = ref(normalize(readPreference(STORAGE_KEY, DEFAULT_LOCALE)))

  function apply() {
    const value = normalize(locale.value)
    locale.value = value
    document.documentElement.lang = value
  }

  function setLocale(next) {
    locale.value = normalize(next)
    writePreference(STORAGE_KEY, locale.value)
    apply()
  }

  function toggle() {
    const index = LOCALES.indexOf(locale.value)
    setLocale(LOCALES[(index + 1) % LOCALES.length])
  }

  function init() {
    apply()
  }

  return { locale, setLocale, toggle, init }
})
