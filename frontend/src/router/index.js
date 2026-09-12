import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppLayout from '@/components/AppLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'login', component: () => import('@/views/LoginView.vue') },
    {
      path: '/',
      component: AppLayout,
      children: [
        { path: 'main', name: 'main', component: () => import('@/views/MainView.vue'), meta: { requiresAuth: true } },
        {
          path: 'roadmap',
          name: 'roadmap',
          component: () => import('@/views/RoadmapView.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'json-formatter',
          name: 'json-formatter',
          component: () => import('@/views/JsonFormatterView.vue')
        },
        {
          path: 'sql-sandbox',
          name: 'sql-sandbox',
          component: () => import('@/views/SqlSandboxView.vue')
        },
        {
          path: 'products-ui',
          name: 'products-ui',
          component: () => import('@/views/ProductsView.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('@/views/ProfileView.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'collections',
          name: 'collections',
          component: () => import('@/views/CollectionsView.vue')
        },
        {
          path: 'mocks',
          name: 'mocks',
          component: () => import('@/views/MocksView.vue')
        },
        {
          path: 'notes',
          name: 'notes',
          component: () => import('@/views/NotesView.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router
