import React, { useState } from 'react';
import UserList from './components/UserList';
import Auth from './components/Auth';

function App() {
  // Estado para controlar quién está usando la aplicación
  const [usuarioSesion, setUsuarioSesion] = useState(null);

  // Si no hay usuario en sesión, mostramos la pantalla de Auth
  if (!usuarioSesion) {
    return <Auth onLoginSuccess={(user) => setUsuarioSesion(user)} />;
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-gray-800 p-4 shadow-lg flex justify-between items-center">
        <h1 className="text-white text-xl font-bold">Admin Panel Empresarial</h1>
        
        {/* Mostramos quién está logueado y un botón para salir */}
        <div className="flex items-center space-x-4">
          <span className="text-gray-300 text-sm">
            Hola, <b className="text-white">{usuarioSesion.primerNombre}</b> ({usuarioSesion.rol})
          </span>
          <button 
            onClick={() => setUsuarioSesion(null)} 
            className="text-sm bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
          >
            Cerrar Sesión
          </button>
        </div>
      </nav>
      
      <main>
        {/* Le pasamos a la tabla quién es el usuario actual para que aplique los roles */}
        <UserList usuarioSesion={usuarioSesion} />
      </main>
    </div>
  );
}

export default App;