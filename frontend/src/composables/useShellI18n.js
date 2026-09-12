import { computed } from 'vue'
import { MESSAGES, DEFAULT_LOCALE } from '@/i18n/messages'
import { useLocaleStore } from '@/stores/locale'

export function useShellI18n() {
  const localeStore = useLocaleStore()
  const locale = computed(() => localeStore.locale)

  function t(key) {
    const dict = MESSAGES[localeStore.locale] || MESSAGES[DEFAULT_LOCALE]
    return dict[key] ?? MESSAGES[DEFAULT_LOCALE][key] ?? key
  }

  return { t, locale }
}
