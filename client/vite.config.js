import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // Necesario para que Docker exponga la IP
    port: 5173,
    watch: {
      usePolling: true, // ¡La línea mágica! Obliga a Vite a vigilar los archivos en Docker
    },
    hmr: {
      clientPort: 5173, // Asegura que la conexión del Hot-Reload no se pierda por los puertos
    }
  }
})