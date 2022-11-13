import router from './router'
import {ElMessage} from 'element-plus'


const whiteList = ['/login', '/auth-redirect', '/bind', '/register'];

const getToken = () => {

}

router.beforeEach((to, from, next) => {
    if (getToken()) {
        if (to.path === '/login') {
            next({path: '/'})
        } else {
            // 判断权限
            next()
        }
    } else {
        // 没有token
        if (whiteList.indexOf(to.path) !== -1) {
            // 在免登录白名单，直接进入
            next()
        } else {
            next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
        }
    }
})

router.afterEach(() => {
})
