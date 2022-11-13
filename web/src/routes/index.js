import { createWebHistory, createRouter } from 'vue-router'

// 公共路由
export const constantRoutes = [
    // {
    //     path: '/login',
    //     component: () => import(''),
    //     hidden: true
    // },
    // {
    //     path: '/404',
    //     component: () => import(''),
    //     hidden: true
    // },
    // {
    //     path: '/401',
    //     component: () => import(''),
    //     hidden: true
    // },
    // {
    //     path: '/',
    //     component: ()=>import('../App.vue'),
    // },
    {
        path: '/',
        component: ()=>import('../views/Home.vue'),
    },
    {
        path: '/classroom',
        component: ()=>import('../views/classroom/index.vue'),
    },
    {
        path: '/classroom1',
        component: ()=>import('../views/classroom/whiteboard.vue'),
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return { top: 0 }
        }
    },
});

export default router;
