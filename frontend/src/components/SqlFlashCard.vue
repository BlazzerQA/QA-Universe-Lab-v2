<template>
  <article
    class="flash"
    :class="`flash--${variant}`"
    :data-testid="testId"
    @pointerdown="onPointerDown"
    @pointerup="onPointerUp"
  >
    <header class="flash-h">
      <span class="flash-kicker">
        <SqlGlyph :name="glyph" :size="13" />
        {{ label }}
      </span>
      <span class="flash-step">{{ index + 1 }}/{{ items.length }}</span>
    </header>
    <p class="flash-text">{{ items[index] }}</p>
    <div class="flash-nav">
      <button
        type="button"
        class="flash-arrow"
        :aria-label="prevLabel"
        :data-testid="prevTestId"
        @click="shift(-1)"
      >
        <SqlGlyph name="chevron" :size="12" />
      </button>
      <div class="flash-dots" role="tablist" :aria-label="dotsLabel">
        <button
          v-for="(_, dotIndex) in items"
          :key="dotIndex"
          type="button"
          class="flash-dot"
          :class="{ 'is-on': dotIndex === index }"
          :aria-label="`${label} ${dotIndex + 1}`"
          @click="index = dotIndex"
        />
      </div>
      <button
        type="button"
        class="flash-arrow flash-arrow--next"
        :aria-label="nextLabel"
        :data-testid="nextTestId"
        @click="shift(1)"
      >
        <SqlGlyph name="chevron" :size="12" />
      </button>
    </div>
  </article>
</template>

<script setup>
import { ref, watch } from 'vue'
import SqlGlyph from '@/components/SqlGlyph.vue'

const props = defineProps({
  variant: { type: String, required: true },
  label: { type: String, required: true },
  glyph: { type: String, required: true },
  items: { type: Array, default: () => [] },
  testId: { type: String, default: '' },
  prevTestId: { type: String, default: '' },
  nextTestId: { type: String, default: '' },
  prevLabel: { type: String, default: 'Назад' },
  nextLabel: { type: String, default: 'Вперёд' },
  dotsLabel: { type: String, default: 'Слайды' }
})

const index = ref(0)
const startX = ref(0)

watch(
  () => props.items,
  () => {
    index.value = 0
  }
)

function shift(delta) {
  const total = props.items.length
  if (!total) return
  index.value = (index.value + delta + total) % total
}

function onPointerDown(event) {
  startX.value = event.clientX
}

function onPointerUp(event) {
  const dx = event.clientX - startX.value
  if (dx > 40) shift(-1)
  if (dx < -40) shift(1)
}
</script>

<style scoped>
.flash {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-height: 108px;
  padding: 9px 10px 8px;
  border-radius: 12px;
  user-select: none;
  touch-action: pan-y;
}

.flash--task {
  color: var(--heading);
  background:
    radial-gradient(120px 80px at 100% 0%, rgba(251, 191, 36, 0.22), transparent 70%),
    linear-gradient(165deg, rgba(245, 158, 11, 0.2), rgba(120, 53, 15, 0.12));
  border: 1px solid rgba(251, 191, 36, 0.42);
  box-shadow: inset 0 1px 0 rgba(253, 230, 138, 0.16);
}

.flash--hint {
  color: var(--heading);
  background:
    radial-gradient(120px 80px at 0% 0%, rgba(167, 139, 250, 0.24), transparent 70%),
    linear-gradient(165deg, rgba(139, 92, 246, 0.22), rgba(49, 46, 129, 0.12));
  border: 1px solid rgba(167, 139, 250, 0.42);
  box-shadow: inset 0 1px 0 rgba(196, 181, 253, 0.14);
}

.flash-h {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.flash-kicker {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.flash--task .flash-kicker,
.flash--task .flash-step {
  color: #fbbf24;
}

.flash--hint .flash-kicker,
.flash--hint .flash-step {
  color: #c4b5fd;
}

.flash-step {
  font-family: var(--mono);
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0;
}

.flash-text {
  margin: 0;
  flex: 1;
  color: var(--text);
  font-size: 0.8rem;
  line-height: 1.4;
}

.flash-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-top: 2px;
}

.flash-arrow {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  padding: 0;
  border: 1px solid transparent;
  border-radius: 999px;
  background: rgba(15, 23, 42, 0.28);
  color: inherit;
  cursor: pointer;
}

.flash-arrow :deep(.sql-glyph) {
  transform: rotate(180deg);
}

.flash-arrow--next :deep(.sql-glyph) {
  transform: none;
}

.flash--task .flash-arrow {
  color: #fde68a;
  border-color: rgba(251, 191, 36, 0.35);
}

.flash--hint .flash-arrow {
  color: #ddd6fe;
  border-color: rgba(167, 139, 250, 0.35);
}

.flash-arrow:hover {
  background: rgba(255, 255, 255, 0.08);
}

.flash-dots {
  display: flex;
  justify-content: center;
  gap: 5px;
}

.flash-dot {
  width: 6px;
  height: 6px;
  padding: 0;
  border: 0;
  border-radius: 999px;
  background: rgba(148, 163, 184, 0.45);
  cursor: pointer;
}

.flash--task .flash-dot.is-on {
  background: #fbbf24;
}

.flash--hint .flash-dot.is-on {
  background: #c4b5fd;
}

:global([data-theme='light']) .flash--task {
  background:
    radial-gradient(120px 80px at 100% 0%, rgba(251, 191, 36, 0.35), transparent 70%),
    linear-gradient(165deg, #fff7ed, #ffedd5);
  border-color: #fdba74;
}

:global([data-theme='light']) .flash--hint {
  background:
    radial-gradient(120px 80px at 0% 0%, rgba(167, 139, 250, 0.28), transparent 70%),
    linear-gradient(165deg, #f5f3ff, #ede9fe);
  border-color: #c4b5fd;
}

:global([data-theme='light']) .flash--task .flash-kicker,
:global([data-theme='light']) .flash--task .flash-step {
  color: #b45309;
}

:global([data-theme='light']) .flash--hint .flash-kicker,
:global([data-theme='light']) .flash--hint .flash-step {
  color: #6d28d9;
}
</style>
