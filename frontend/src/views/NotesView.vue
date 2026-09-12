<template>
  <div class="notes-page ui-page" data-testid="notes-page">
    <main class="page-layout">
      <aside class="sidebar ui-panel" data-testid="notes-sidebar">
        <nav class="sidebar-nav" aria-label="Knowledge categories">
          <h2>Knowledge</h2>
          <div class="sidebar-content">
            <p v-if="sidebarState === 'loading'" class="state-message ui-muted">Loading notes...</p>
            <p v-else-if="sidebarState === 'empty'" class="state-message ui-muted">No notes available.</p>
            <p v-else-if="sidebarState === 'error'" class="state-message ui-hint ui-hint--error">
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
.page-layout {
  display: grid;
  grid-template-columns: 260px 1fr;
  min-height: calc(100vh - var(--header-h));
  padding-top: var(--header-h);
}

.sidebar {
  border-radius: 0;
  border-top: none;
  border-left: none;
  border-bottom: none;
  padding: 1.5rem 1rem;
  overflow-y: auto;
}

.sidebar-nav h2 {
  margin: 0 0 1rem 0;
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  color: var(--muted);
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
  color: var(--primary);
  border-bottom: 1px solid var(--border);
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
  color: var(--text);
  text-decoration: none;
  font-size: 0.95rem;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.note-link:hover,
.note-link:focus {
  background-color: var(--primary-muted);
  color: var(--primary);
}

.note-link.active {
  background-color: var(--primary-muted);
  color: var(--primary);
  font-weight: 600;
}

.state-message {
  margin: 0;
  padding: 1rem 0.5rem;
  text-align: center;
  font-size: 0.95rem;
  border-radius: 8px;
}

.content-area {
  padding: 2rem;
  overflow-y: auto;
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
  border-radius: 8px;
  padding: 1rem;
  overflow-x: auto;
  margin: 0 0 1rem 0;
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
    grid-template-columns: 1fr;
  }

  .sidebar {
    border-right: none;
    border-bottom: 1px solid var(--border);
  }

  .content-area {
    padding: 1.5rem;
  }
}
</style>
