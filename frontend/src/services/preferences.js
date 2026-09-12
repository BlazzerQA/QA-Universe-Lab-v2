/** Persistence behind a tiny API so stores do not talk to localStorage directly (DIP). */

export function readPreference(key, fallback) {
  try {
    const value = localStorage.getItem(key)
    return value == null ? fallback : value
  } catch {
    return fallback
  }
}

export function writePreference(key, value) {
  try {
    localStorage.setItem(key, value)
  } catch {
    /* private mode / quota */
  }
}
