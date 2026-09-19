<template>
  <aside class="notes-sidebar ui-panel" data-testid="notes-sidebar">
    <nav class="sidebar-nav" aria-label="Knowledge categories">
      <div class="sidebar-toolbar">
        <div class="sidebar-heading">
          <h2>Knowledge</h2>
          <span v-if="totalCount > 0" class="notes-total" data-testid="notes-total">{{ totalCount }}</span>
        </div>

        <div v-if="sidebarState === 'ready'" class="sidebar-controls">
          <UiInput
            :model-value="searchQuery"
            input-id="notes-search"
            type="search"
            placeholder="Search notes…"
            aria-label="Search notes"
            data-testid="notes-search"
            autocomplete="off"
            @update:model-value="$emit('update:searchQuery', $event)"
          />

          <p v-if="isSearching" class="results-meta ui-muted" data-testid="notes-results-meta">
            {{ visibleCount }} of {{ totalCount }}
          </p>
        </div>
      </div>

      <div class="sidebar-content">
        <p v-if="sidebarState === 'loading'" class="state-message ui-muted">Loading notes...</p>
        <p v-else-if="sidebarState === 'empty'" class="state-message ui-muted">No notes available.</p>
        <p v-else-if="sidebarState === 'error'" class="state-message ui-hint ui-hint--error">
          Failed to load notes.<br />Please try again later.
        </p>
        <p v-else-if="!groups.length" class="state-message ui-muted" data-testid="notes-no-matches">
          No matching notes.
        </p>
        <section v-else v-for="group in groups" :key="group.category" class="category-section">
          <button
            type="button"
            class="category-toggle"
            :aria-expanded="isExpanded(group.category)"
            :data-testid="'notes-group-' + group.category"
            @click="$emit('toggle-category', group.category)"
          >
            <span class="category-chevron" aria-hidden="true">{{ isExpanded(group.category) ? '▾' : '▸' }}</span>
            <img
              v-if="categoryLogo(group.category)"
              :src="categoryLogo(group.category)"
              alt=""
              class="category-logo"
              aria-hidden="true"
            />
            <span class="category-title">{{ group.category.toUpperCase() }}</span>
            <span class="category-count">{{ group.notes.length }}</span>
          </button>
          <ul v-if="isExpanded(group.category)" class="note-list">
            <li v-for="note in group.notes" :key="note.path">
              <a
                href="#"
                class="note-link"
                :class="{ active: activePath === note.path }"
                :data-testid="'note-link-' + note.path"
                @click.prevent="$emit('select-note', note)"
              >
                <span class="note-link-title">{{ note.title }}</span>
                <span v-if="noteSubtitle(note)" class="note-link-subtitle">{{ noteSubtitle(note) }}</span>
              </a>
            </li>
          </ul>
        </section>
      </div>
    </nav>
  </aside>
</template>

<script setup>
import UiInput from '@/components/ui/UiInput.vue'
import { categoryLogo } from '@/config/noteCategories'

const props = defineProps({
  sidebarState: { type: String, required: true },
  searchQuery: { type: String, default: '' },
  groups: { type: Array, default: () => [] },
  expandedCategories: { type: Object, default: () => ({}) },
  activePath: { type: String, default: '' },
  totalCount: { type: Number, default: 0 },
  visibleCount: { type: Number, default: 0 },
  isSearching: { type: Boolean, default: false }
})

defineEmits(['update:searchQuery', 'toggle-category', 'select-note'])

function isExpanded(category) {
  return props.isSearching || !!props.expandedCategories[category]
}

function noteSubtitle(note) {
  const path = (note.path || '').replace(/\\/g, '/')
  const category = note.category || ''
  let rest = path
  if (category && rest.startsWith(`${category}/`)) {
    rest = rest.slice(category.length + 1)
  }
  const parts = rest.split('/').filter(Boolean)
  if (parts.length <= 1) return ''
  parts.pop()
  return parts.join('/')
}
</script>

<style scoped>
.notes-sidebar {
  border-radius: 0;
  border-top: none;
  border-left: none;
  border-bottom: none;
  padding: 1.25rem 0.85rem;
  overflow-y: auto;
  min-height: 0;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  min-height: 100%;
}

.sidebar-toolbar {
  position: sticky;
  top: -1.25rem;
  z-index: 1;
  margin: -1.25rem -0.85rem 0;
  padding: 1.25rem 0.85rem 0.85rem;
  background: var(--panel);
}

.sidebar-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.75rem;
  margin-bottom: 0.85rem;
}

.sidebar-heading h2 {
  margin: 0;
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  color: var(--muted);
}

.notes-total {
  min-width: 1.5rem;
  padding: 0.1rem 0.45rem;
  border-radius: 999px;
  background: var(--primary-muted);
  color: var(--primary);
  font-size: 0.75rem;
  font-weight: 700;
  text-align: center;
}

.sidebar-controls :deep(.ui-field) {
  margin-bottom: 0;
}

.results-meta {
  margin: 0.65rem 0 0;
  font-size: 0.78rem;
}

.sidebar-content {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
  padding-top: 0.35rem;
}

.category-section {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.category-toggle {
  display: flex;
  align-items: center;
  gap: 0.45rem;
  width: 100%;
  margin: 0;
  padding: 0.4rem 0.35rem;
  border: 0;
  border-bottom: 1px solid var(--border);
  border-radius: 0;
  background: transparent;
  color: var(--primary);
  cursor: pointer;
  text-align: left;
}

.category-toggle:hover {
  background: var(--primary-muted);
}

.category-chevron {
  width: 0.9rem;
  flex-shrink: 0;
  font-size: 0.75rem;
  color: var(--muted);
}

.category-logo {
  width: 1.15rem;
  height: 1.15rem;
  flex-shrink: 0;
  object-fit: contain;
  border-radius: 3px;
}

.category-title {
  flex: 1;
  min-width: 0;
  font-size: 0.78rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.category-count {
  font-size: 0.72rem;
  font-weight: 700;
  color: var(--muted);
  font-variant-numeric: tabular-nums;
}

.note-list {
  list-style: none;
  margin: 0;
  padding: 0.15rem 0 0.35rem;
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
}

.note-link-title {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.note-link {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  padding: 0.38rem 0.6rem;
  border-radius: var(--radius-sm);
  color: var(--text);
  text-decoration: none;
  font-size: 0.9rem;
  line-height: 1.3;
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

.note-link-subtitle {
  font-size: 0.72rem;
  font-weight: 500;
  color: var(--muted);
}

.note-link.active .note-link-subtitle,
.note-link:hover .note-link-subtitle {
  color: var(--primary);
  opacity: 0.8;
}

.state-message {
  margin: 0;
  padding: 1rem 0.5rem;
  text-align: center;
  font-size: 0.95rem;
  border-radius: 8px;
}
</style>
