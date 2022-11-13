import {createWebHistory, createRouter} from 'vue-router'

// 公共路由
export const constantRoutes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/home',
        component: () => import('../views/Home.vue'),
    },
    {
        path: '/classroom',
        component: () => import('../views/classroom/index.vue'),
    },
    {
        path: '/login',
        component: () => import('../views/login.vue'),
    },
    {
        path: '/registered',
        component: () => import('../views/registered.vue'),
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return {top: 0}
        }
    },
});

export default router;
