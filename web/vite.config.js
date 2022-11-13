import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  proxy:{
    '':{
      target:'',
      changeOrigin:true,  // 允许跨域
    }
  }
})
