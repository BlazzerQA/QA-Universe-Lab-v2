<template>
  <div class="notes-page" data-testid="notes-page">
    <main class="page-layout">
      <aside class="sidebar" data-testid="notes-sidebar">
        <nav class="sidebar-nav" aria-label="Knowledge categories">
          <h2>Knowledge</h2>
          <div class="sidebar-content">
            <p v-if="sidebarState === 'loading'" class="state-message loading-state">Loading notes...</p>
            <p v-else-if="sidebarState === 'empty'" class="state-message empty-state">No notes available.</p>
            <p v-else-if="sidebarState === 'error'" class="state-message error-state">
              Failed to load notes.<br />Please try again later.
            </p>
            <section v-else v-for="(notes, category) in groupedNotes" :key="category" class="category-section">
              <h3 class="category-title">{{ category.toUpperCase() }}</h3>
              <ul class="note-list">
                <li v-for="note in notes" :key="note.path">
                  <a
                    href="#"
                    class="note-link"
                    :class="{ active: activePath === note.path }"
                    :data-testid="'note-link-' + note.path"
                    @click.prevent="selectNote(note)"
                  >
                    {{ note.title }}
                  </a>
                </li>
              </ul>
            </section>
          </div>
        </nav>
      </aside>

      <section class="content-area" ref="contentArea" aria-label="Note content" data-testid="notes-content">
        <article v-if="noteState === 'placeholder'" class="note-content note-placeholder">
          <p>Выберите статью из меню</p>
        </article>
        <article v-else-if="noteState === 'loading'" class="note-content">
          <p class="note-state loading-state">Загрузка заметки...</p>
        </article>
        <article v-else-if="noteState === 'error'" class="note-content">
          <p class="note-state error-state">{{ noteError }}</p>
        </article>
        <article v-else class="note-content" data-testid="note-body">
          <div class="note-body" v-html="noteHtml"></div>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import api from '@/api/client'

const groupedNotes = ref({})
const sidebarState = ref('loading')
const noteState = ref('placeholder')
const noteHtml = ref('')
const noteError = ref('')
const activePath = ref('')
const contentArea = ref(null)

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
    .reduce((acc, category) => {
      acc[category] = grouped[category].sort((a, b) => a.title.localeCompare(b.title))
      return acc
    }, {})
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
      sidebarState.value = 'empty'
      return
    }
    groupedNotes.value = groupByCategory(notes)
    sidebarState.value = 'ready'
  } catch (error) {
    console.error('Failed to load notes:', error)
    sidebarState.value = 'error'
  }
}

async function selectNote(note) {
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

onMounted(() => {
  loadNotes()
})
</script>

<style scoped>
.notes-page {
  min-height: 100vh;
  background-color: #121212;
  color: #e0e0e0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  line-height: 1.6;
}

.page-layout {
  display: grid;
  grid-template-columns: 260px 1fr;
  min-height: calc(100vh - 80px);
  padding-top: 80px;
}

.sidebar {
  background-color: #1a1a1a;
  border-right: 1px solid rgba(255, 255, 255, 0.08);
  padding: 1.5rem 1rem;
  overflow-y: auto;
}

.sidebar-nav h2 {
  margin: 0 0 1rem 0;
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  color: #888;
}

.sidebar-content {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.category-section {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.category-title {
  margin: 0;
  font-size: 0.8rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  color: #00ff41;
  border-bottom: 1px solid rgba(0, 255, 65, 0.2);
  padding-bottom: 0.3rem;
}

.note-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.note-link {
  display: block;
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  color: #e0e0e0;
  text-decoration: none;
  font-size: 0.95rem;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.note-link:hover,
.note-link:focus {
  background-color: rgba(0, 255, 65, 0.1);
  color: #00ff41;
}

.note-link.active {
  background-color: rgba(0, 255, 65, 0.2);
  color: #00ff41;
  font-weight: 600;
}

.state-message {
  margin: 0;
  padding: 1rem 0.5rem;
  text-align: center;
  font-size: 0.95rem;
  border-radius: 8px;
}

.loading-state {
  color: #888;
}

.empty-state {
  color: #bbb;
  background-color: rgba(255, 255, 255, 0.03);
}

.error-state {
  color: #ff6b6b;
  background-color: rgba(255, 107, 107, 0.08);
}

.content-area {
  padding: 2rem;
  overflow-y: auto;
}

.note-content {
  background-color: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 2rem;
  min-height: 100%;
}

.note-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100%;
  color: #888;
  font-size: 1.1rem;
  text-align: center;
}

.note-state {
  margin: 0;
  text-align: center;
  font-size: 1.1rem;
}

.note-body :deep(h1),
.note-body :deep(h2),
.note-body :deep(h3),
.note-body :deep(h4),
.note-body :deep(h5),
.note-body :deep(h6) {
  color: #00ff41;
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
  background-color: rgba(255, 255, 255, 0.08);
  color: #00ff41;
  padding: 0.15rem 0.35rem;
  border-radius: 4px;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 0.9em;
}

.note-body :deep(pre) {
  background-color: #121212;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 8px;
  padding: 1rem;
  overflow-x: auto;
  margin: 0 0 1rem 0;
}

.note-body :deep(pre code) {
  background-color: transparent;
  padding: 0;
  color: #e0e0e0;
}

.note-body :deep(blockquote) {
  border-left: 3px solid #00ff41;
  margin: 0 0 1rem 0;
  padding-left: 1rem;
  color: #bbb;
}

.note-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

.note-body :deep(th),
.note-body :deep(td) {
  border: 1px solid rgba(255, 255, 255, 0.1);
  padding: 0.5rem 0.75rem;
  text-align: left;
}

.note-body :deep(th) {
  background-color: rgba(0, 255, 65, 0.1);
  color: #00ff41;
}

@media (max-width: 768px) {
  .page-layout {
    grid-template-columns: 1fr;
  }

  .sidebar {
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  }

  .content-area {
    padding: 1.5rem;
  }
}
</style>
