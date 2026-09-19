<template>
  <div class="notes-page ui-page" data-testid="notes-page">
    <main class="page-layout">
      <div class="mobile-toolbar" data-testid="notes-mobile-toolbar">
        <button
          type="button"
          class="ui-btn ui-btn--ghost browse-btn"
          data-testid="notes-browse"
          :aria-expanded="notesNavOpen"
          aria-controls="notes-sidebar-panel"
          @click="notesNavOpen = !notesNavOpen"
        >
          {{ notesNavOpen ? 'Close notes' : 'Browse notes' }}
        </button>
        <p class="current-note ui-muted">{{ currentNoteLabel }}</p>
      </div>

      <div
        id="notes-sidebar-panel"
        class="sidebar-shell"
        :class="{ 'is-open': notesNavOpen }"
      >
        <NotesSidebar
          :sidebar-state="sidebarState"
          :search-query="searchQuery"
          :groups="visibleGroups"
          :expanded-categories="expandedCategories"
          :active-path="activePath"
          :total-count="allNotes.length"
          :visible-count="visibleCount"
          :is-searching="isSearching"
          @update:search-query="searchQuery = $event"
          @toggle-category="toggleCategory"
          @select-note="selectNote"
        />
      </div>

      <section class="content-area" ref="contentArea" aria-label="Note content" data-testid="notes-content">
        <article v-if="noteState === 'placeholder'" class="note-content ui-card note-placeholder">
          <p class="ui-muted">Выберите статью из меню</p>
        </article>
        <article v-else-if="noteState === 'loading'" class="note-content ui-card">
          <p class="note-state ui-muted">Загрузка заметки...</p>
        </article>
        <article v-else-if="noteState === 'error'" class="note-content ui-card">
          <p class="note-state ui-hint ui-hint--error">{{ noteError }}</p>
        </article>
        <article v-else class="note-content ui-card" data-testid="note-body">
          <div class="note-body" v-html="noteHtml"></div>
        </article>
      </section>

      <button
        v-if="notesNavOpen"
        type="button"
        class="notes-overlay"
        aria-label="Close notes"
        data-testid="notes-sidebar-overlay"
        @click="notesNavOpen = false"
      />
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/client'
import NotesSidebar from '@/components/NotesSidebar.vue'

const route = useRoute()
const router = useRouter()

const allNotes = ref([])
const sidebarState = ref('loading')
const noteState = ref('placeholder')
const noteHtml = ref('')
const noteError = ref('')
const activePath = ref('')
const contentArea = ref(null)
const searchQuery = ref('')
const expandedCategories = ref({})
const notesNavOpen = ref(false)

const isSearching = computed(() => searchQuery.value.trim().length > 0)

const groupedNotes = computed(() => groupByCategory(allNotes.value))

const visibleGroups = computed(() => {
  const query = searchQuery.value.trim().toLowerCase()
  return groupedNotes.value
    .map((group) => {
      const notes = query ? group.notes.filter((note) => matchesQuery(note, query)) : group.notes
      if (!notes.length) return null
      return { category: group.category, notes }
    })
    .filter(Boolean)
})

const visibleCount = computed(() =>
  visibleGroups.value.reduce((sum, group) => sum + group.notes.length, 0)
)

const currentNoteLabel = computed(() => {
  if (!activePath.value) return 'Knowledge'
  const current = allNotes.value.find((note) => note.path === activePath.value)
  return current?.title || 'Knowledge'
})

function matchesQuery(note, query) {
  return [note.title, note.category, note.path]
    .filter(Boolean)
    .some((value) => value.toLowerCase().includes(query))
}

function groupByCategory(notes) {
  const grouped = notes.reduce((acc, note) => {
    const category = note.category || 'Uncategorized'
    if (!acc[category]) {
      acc[category] = []
    }
    acc[category].push(note)
    return acc
  }, {})

  return Object.keys(grouped)
    .sort((a, b) => a.localeCompare(b))
    .map((category) => ({
      category,
      notes: grouped[category].sort((a, b) => a.title.localeCompare(b.title))
    }))
}

function expandCategory(category) {
  if (!category || expandedCategories.value[category]) return
  expandedCategories.value = { ...expandedCategories.value, [category]: true }
}

function toggleCategory(category) {
  expandedCategories.value = {
    ...expandedCategories.value,
    [category]: !expandedCategories.value[category]
  }
}

function buildNoteApiPath(path) {
  const normalizedPath = path.replace(/\\/g, '/')
  const firstSlashIndex = normalizedPath.indexOf('/')

  let category
  let fileName
  if (firstSlashIndex === -1) {
    category = ''
    fileName = normalizedPath
  } else {
    category = normalizedPath.substring(0, firstSlashIndex)
    fileName = normalizedPath.substring(firstSlashIndex + 1)
  }

  const noteName = fileName.replace(/\.md$/i, '')
  return `/api/notes/${encodeURIComponent(category)}/${encodeURIComponent(noteName)}`
}

async function loadNotes() {
  sidebarState.value = 'loading'
  try {
    const response = await api.get('/api/notes')
    const notes = response.data
    if (!Array.isArray(notes) || notes.length === 0) {
      allNotes.value = []
      sidebarState.value = 'empty'
      return
    }
    allNotes.value = notes
    sidebarState.value = 'ready'
    await applyPathFromQuery(route.query.path)
  } catch (error) {
    console.error('Failed to load notes:', error)
    sidebarState.value = 'error'
  }
}

function resetNoteView() {
  activePath.value = ''
  noteHtml.value = ''
  noteError.value = ''
  noteState.value = 'placeholder'
}

async function applyPathFromQuery(path) {
  if (!path) {
    if (activePath.value) resetNoteView()
    return
  }
  if (path === activePath.value && (noteState.value === 'ready' || noteState.value === 'loading')) {
    return
  }
  const note = allNotes.value.find((item) => item.path === path)
  if (!note) {
    noteError.value = 'Note not found.'
    noteState.value = 'error'
    activePath.value = path
    return
  }
  expandCategory(note.category || 'Uncategorized')
  await loadNoteContent(note)
}

async function loadNoteContent(note) {
  activePath.value = note.path
  noteState.value = 'loading'
  try {
    const response = await api.get(buildNoteApiPath(note.path))
    noteHtml.value = response.data.content || ''
    noteState.value = 'ready'
    if (contentArea.value) {
      contentArea.value.scrollTo({ top: 0, behavior: 'smooth' })
    }
  } catch (error) {
    if (error.response?.status === 404) {
      noteError.value = 'Note not found.'
    } else {
      noteError.value = 'Failed to load note. Please try again later.'
    }
    noteState.value = 'error'
  }
}

async function selectNote(note) {
  notesNavOpen.value = false
  expandCategory(note.category || 'Uncategorized')
  await loadNoteContent(note)
  if (route.query.path !== note.path) {
    await router.replace({ query: { ...route.query, path: note.path } })
  }
}

function onResize() {
  if (window.innerWidth > 768) notesNavOpen.value = false
}

watch(
  () => route.query.path,
  (path) => {
    if (sidebarState.value !== 'ready') return
    applyPathFromQuery(path)
  }
)

onMounted(() => {
  loadNotes()
  window.addEventListener('resize', onResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})
</script>

<style scoped>
.notes-page {
  min-height: calc(100vh - var(--header-h));
  --notes-toolbar-h: 60px;
}

.page-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  height: calc(100vh - var(--header-h));
  min-height: 0;
}

.mobile-toolbar {
  display: none;
}

.sidebar-shell {
  min-height: 0;
  min-width: 0;
}

.sidebar-shell :deep(.notes-sidebar) {
  height: 100%;
}

.content-area {
  padding: 2rem;
  overflow-y: auto;
  min-width: 0;
}

.note-content {
  min-height: 100%;
}

.note-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100%;
  font-size: 1.1rem;
  text-align: center;
}

.note-state {
  margin: 0;
  text-align: center;
  font-size: 1.1rem;
}

.notes-overlay {
  display: none;
}

.note-body :deep(h1),
.note-body :deep(h2),
.note-body :deep(h3),
.note-body :deep(h4),
.note-body :deep(h5),
.note-body :deep(h6) {
  color: var(--heading);
  margin-top: 1.5rem;
  margin-bottom: 0.75rem;
}

.note-body :deep(p) {
  margin: 0 0 1rem 0;
}

.note-body :deep(ul),
.note-body :deep(ol) {
  margin: 0 0 1rem 0;
  padding-left: 1.5rem;
}

.note-body :deep(code) {
  background-color: var(--primary-muted);
  color: var(--syntax-key);
  padding: 0.15rem 0.35rem;
  border-radius: 4px;
  font-family: var(--mono);
  font-size: 0.9em;
}

.note-body :deep(pre) {
  background-color: var(--bg);
  border: 1px solid var(--border);
  padding: 1rem;
  overflow-x: auto;
  margin: 0 0 1rem 0;
  border-radius: 8px;
}

.note-body :deep(pre code) {
  background-color: transparent;
  padding: 0;
  color: var(--text);
}

.note-body :deep(blockquote) {
  border-left: 3px solid var(--primary);
  margin: 0 0 1rem 0;
  padding-left: 1rem;
  color: var(--muted);
}

.note-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

.note-body :deep(th),
.note-body :deep(td) {
  border: 1px solid var(--border);
  padding: 0.5rem 0.75rem;
  text-align: left;
}

.note-body :deep(th) {
  background-color: var(--primary-muted);
  color: var(--heading);
}

.note-body :deep(a) {
  color: var(--primary);
}

@media (max-width: 768px) {
  .page-layout {
    display: flex;
    flex-direction: column;
    height: calc(100vh - var(--header-h));
  }

  .mobile-toolbar {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 0.75rem 1rem;
    border-bottom: 1px solid var(--border);
    background: var(--panel);
    flex-shrink: 0;
    position: relative;
    z-index: 37;
  }

  .browse-btn {
    flex-shrink: 0;
    padding: 8px 12px;
    font-size: 0.85rem;
  }

  .current-note {
    margin: 0;
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size: 0.9rem;
  }

  .sidebar-shell {
    position: fixed;
    top: calc(var(--header-h) + var(--notes-toolbar-h));
    left: 0;
    bottom: 0;
    width: min(320px, 88vw);
    z-index: 36;
    transform: translateX(-100%);
    transition: transform 0.2s ease;
    box-shadow: none;
  }

  .sidebar-shell.is-open {
    transform: translateX(0);
    box-shadow: var(--shadow);
  }

  .sidebar-shell :deep(.notes-sidebar) {
    height: 100%;
    border-right: 1px solid var(--border);
  }

  .content-area {
    padding: 1.5rem;
    flex: 1;
    min-height: 0;
  }

  .notes-overlay {
    display: block;
    position: fixed;
    top: calc(var(--header-h) + var(--notes-toolbar-h));
    right: 0;
    bottom: 0;
    left: 0;
    border: 0;
    padding: 0;
    background: rgba(0, 0, 0, 0.45);
    z-index: 35;
    cursor: pointer;
  }
}
</style>
