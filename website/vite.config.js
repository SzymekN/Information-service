import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
const fs = require('fs')

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    proxy: {
      '/client': {
        target: 'http://localhost:8080',
      },
      '/editorial': {
        target: 'http://localhost:8080',
      }
    }
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
