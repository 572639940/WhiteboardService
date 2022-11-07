import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import router from "./routes/index.js";
import {createPinia} from 'pinia'

const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.use(createPinia())

app.mount('#app')
