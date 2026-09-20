<template>
  <svg
    class="sql-glyph"
    :width="size"
    :height="size"
    viewBox="0 0 24 24"
    fill="none"
    aria-hidden="true"
  >
    <g v-if="name === 'schema'" stroke="currentColor" stroke-width="1.7" stroke-linejoin="round">
      <rect x="4" y="4" width="16" height="5" rx="1" />
      <rect x="4" y="10.5" width="16" height="5" rx="1" />
      <rect x="4" y="17" width="16" height="4" rx="1" />
    </g>
    <g v-else-if="name === 'query'" stroke="currentColor" stroke-width="1.7" stroke-linecap="round">
      <path d="M5 19l3.2-1 9.3-9.3a2 2 0 0 0 0-2.8L16.1 4.5a2 2 0 0 0-2.8 0L4 13.8 3 17.2z" />
      <path d="M12.5 6.5l5 5" />
    </g>
    <g v-else-if="name === 'result'" stroke="currentColor" stroke-width="1.7">
      <rect x="4" y="5" width="16" height="14" rx="1.5" />
      <path d="M4 10h16M4 14h16M10 5v14" />
    </g>
    <g v-else-if="name === 'lesson'" stroke="currentColor" stroke-width="1.7" stroke-linejoin="round">
      <path d="M3 10l9-5 9 5-9 5-9-5z" />
      <path d="M7 12.2V16c0 .8 2.2 2.5 5 2.5s5-1.7 5-2.5v-3.8" />
    </g>
    <g v-else-if="name === 'play'" fill="currentColor">
      <path d="M8 6.5v11l10-5.5L8 6.5z" />
    </g>
    <g v-else-if="name === 'bulb'" stroke="currentColor" stroke-width="1.7" stroke-linejoin="round">
      <path d="M8 14.5a4.5 4.5 0 1 1 8 0c-.8.9-1.5 1.8-1.5 3h-5c0-1.2-.7-2.1-1.5-3z" />
      <path d="M10 19.5h4M10.5 21h3" stroke-linecap="round" />
    </g>
    <g v-else-if="name === 'trash'" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">
      <path d="M5 7h14M9 7V5h6v2M8 7l.8 12h6.4L16 7" />
    </g>
    <g v-else-if="name === 'target'" stroke="currentColor" stroke-width="1.7">
      <circle cx="12" cy="12" r="8" />
      <circle cx="12" cy="12" r="3.5" />
    </g>
    <g v-else-if="name === 'check'" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
      <circle cx="12" cy="12" r="8" />
      <path d="M8.5 12.2l2.3 2.3 4.7-5" />
    </g>
    <g v-else-if="name === 'table'" stroke="currentColor" stroke-width="1.7">
      <rect x="4" y="5" width="16" height="14" rx="1.5" />
      <path d="M4 9h16M12 9v10" />
    </g>
    <g v-else-if="name === 'folder'" stroke="currentColor" stroke-width="1.7" stroke-linejoin="round">
      <path d="M4 8.5V18a1.5 1.5 0 0 0 1.5 1.5h13A1.5 1.5 0 0 0 20 18V10a1.5 1.5 0 0 0-1.5-1.5H12L10 6H5.5A1.5 1.5 0 0 0 4 7.5z" />
    </g>
    <g v-else-if="name === 'join'" stroke="currentColor" stroke-width="1.7" stroke-linecap="round">
      <circle cx="7" cy="8" r="2.4" />
      <circle cx="7" cy="16" r="2.4" />
      <circle cx="17" cy="12" r="2.4" />
      <path d="M9.2 8.8L14.7 11.2M9.2 15.2L14.7 12.8" />
    </g>
  </svg>
</template>

<script setup>
defineProps({
  name: { type: String, required: true },
  size: { type: [Number, String], default: 16 }
})
</script>

<style scoped>
.sql-glyph {
  display: block;
  flex-shrink: 0;
}
</style>
